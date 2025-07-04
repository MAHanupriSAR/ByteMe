package users;

import canteenUtils.MenuItem;
import canteenUtils.Order;
import gui.admin.AdminGUIManager;
import main.Data;
import main.Main;
import utils.CustomComparators;
import utils.HelperMethod;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Admin extends User{

    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    public Admin(String name, String email, String password, int lineNumber) {
        super(name, email, password, lineNumber);
    }

    public void functionalities(){
        int choice;
        boolean loop = true;
        while(loop){
            System.out.println("""
            Choose an option:
            0. Log out
            1. Manage Items
            2. Manage Orders
            3. View Daily Sales Report
            4. View GUI
            """);
            choice = HelperMethod.input(0,4);
            switch (choice){
                case -1 -> loop=false;
                case 1 -> itemManagement();
                case 2 -> orderManagement();
                case 3 -> viewDailySales();
                case 4 -> viewGUI();
            }
        }
    }

    private void viewDailySales(){
        System.out.println(Main.canteen.getDailySale());
    }

    private void itemManagement(){
        int choice;
        boolean loop = true;
        while(loop){
            System.out.println("""
            Choose an option:
            0. Go back
            1. Show all items
            2. add new item
            3. update existing item
            4. remove item
            """);
            choice = HelperMethod.input(0,4);
            switch (choice){
                case -1 -> loop=false;
                case 1 -> showItems();
                case 2 -> addNewItems();
                case 3 -> updateItem();
                case 4 -> removeItem();
            }
        }
    }

    private void showItems(){
        int itemCount = 1;
        String category = "";
        for(Map.Entry<String, MenuItem> entry : Main.canteen.getMenu().entrySet()){
            String key = entry.getKey();
            String[] parts = key.split(":");
            String tempCategory = parts[0];
            MenuItem menuItem = entry.getValue();
            if(!category.equals(tempCategory)){
                category = tempCategory;
                System.out.println("Category: "+ category);
            }
            System.out.println(itemCount + ". " + menuItem);
            itemCount++;
        }
    }

    private void addNewItems(){
        System.out.print("Name: ");
        String name = Main.getScanner().nextLine();
        name = HelperMethod.capitalizeFirstLetter(name);

        System.out.print("Category: ");
        String category = Main.getScanner().nextLine();
        category = HelperMethod.capitalizeFirstLetter(category);

        if(menuItemPresent(name)){
            System.out.println("Error: Item \"" + name + "\" already exists.");
            return;
        }

        System.out.print("Price: ");
        int price = Main.getScanner().nextInt();
        Main.getScanner().nextLine();

        System.out.print("Stocks Available: ");
        int stocks = Main.getScanner().nextInt();

        MenuItem menuItem = new MenuItem(name,category,price,stocks);
        Main.canteen.getMenu().put(category+":"+name, menuItem);

        Main.canteen.setTotalItems(Main.canteen.getTotalItems() + 1);

        System.out.println("Item added"); Main.canteen.increaseTotalItems(1);
    }

    private void updateItem() {
        showItems();
        int choice = HelperMethod.input(0, Main.canteen.getTotalItems());
        if (choice == -1) {
            System.out.println("Update cancelled!");
            return;
        }

        MenuItem menuItem = getItemAtIndex(choice);
        String originalKey = menuItem.getCategory() + ":" + menuItem.getName();

        System.out.print("New Name (leave empty to keep current): ");
        String newName = Main.getScanner().nextLine();
        if (!newName.isEmpty()) {
            newName = HelperMethod.capitalizeFirstLetter(newName);
        } else {
            newName = menuItem.getName();
        }

        System.out.print("New Category (leave empty to keep current): ");
        String newCategory = Main.getScanner().nextLine();
        if (!newCategory.isEmpty()) {
            newCategory = HelperMethod.capitalizeFirstLetter(newCategory);
        } else {
            newCategory = menuItem.getCategory();
        }

        System.out.print("New Price (enter -1 to keep current): ");
        int newPrice = Main.getScanner().nextInt();
        Main.getScanner().nextLine();
        if (newPrice != -1) {
            menuItem.setPrice(newPrice);
        }

        System.out.print("Change Stocks (enter -1 to keep current): ");
        int newStock = Main.getScanner().nextInt();
        Main.getScanner().nextLine();
        if (newStock != -1) {
            menuItem.setStocks(newStock);
        }

        String newKey = newCategory + ":" + newName;
        if (!newKey.equals(originalKey)) {
            Main.canteen.getMenu().remove(originalKey);
            menuItem.setName(newName);
            menuItem.setCategory(newCategory);
            Main.canteen.getMenu().put(newKey, menuItem);
        }

        for (Customer customer : Main.getData().getCustomers()) {
            customer.getCart().recalculateTotalPrice();
        }

        System.out.println("Item added!");
    }

    private void removeItem() {
        showItems();
        int choice = HelperMethod.input(0, Main.canteen.getTotalItems());
        if (choice == -1) {
            return;
        }

        MenuItem itemToRemove = getItemAtIndex(choice);
        String keyToRemove = itemToRemove.getCategory() + ":" + itemToRemove.getName();

        denyOrderContainingDeletedItems(itemToRemove);

        Main.canteen.getMenu().remove(keyToRemove);
        Main.canteen.setTotalItems(Main.canteen.getTotalItems() - 1);

        System.out.println("Item removed: " + itemToRemove.getName()); Main.canteen.increaseTotalItems(-1);

        System.out.println("Items after removal: ");
        showItems();
    }

    private void denyOrderContainingDeletedItems(MenuItem itemToRemove) {
        for (Customer customer : Main.getData().getCustomers()) {
            for (Order order : customer.getOrdersByStatus(Order.OrderStatus.PENDING, Order.OrderStatus.OUT_FOR_DELIVERY)) {
                if (order.containsItem(itemToRemove)) {
                    Main.getData().updateOrderStatus(order, Order.OrderStatus.DENIED);
                    System.out.println("Order ID " + order.getOrderID() + " has been denied due to item removal: " + itemToRemove.getName());
                }
            }
        }
    }

    private void orderManagement(){
        int choice;
        boolean loop = true;
        while(loop){
            System.out.println("""
                    Choose an option:
                    0. Go to Main Menu
                    1. View Pending Orders
                    2. View Out for Delivery Orders
                    3. View Completed Orders
                    4. View Cancelled Orders
                    5. View Denied Orders
                    6. Process Refunds
                    """);
            choice = HelperMethod.input(0, 6);
            switch (choice){
                case -1 -> loop=false;
                case 1 -> {
                    displayOrderByStatus(Order.OrderStatus.PENDING);
                    pendingOrderOperations();
                }
                case 2 -> displayOrderByStatus(Order.OrderStatus.OUT_FOR_DELIVERY);
                case 3 -> displayOrderByStatus(Order.OrderStatus.COMPLETED);
                case 4 -> {
                    displayOrderByStatus(Order.OrderStatus.CANCELLED);
                    refundOperations(getOrdersByStatus(Order.OrderStatus.CANCELLED));
                }
                case 5 -> {
                    displayOrderByStatus(Order.OrderStatus.DENIED);
                    refundOperations(getOrdersByStatus(Order.OrderStatus.COMPLETED));
                }
                case 6 -> {
                    displayRemainingRefunds();
                    refundOperations(getRemainingRefunds());
                }
            }
        }
    }

    private void pendingOrderOperations(){
        int choice;
        System.out.println("""
                Choose an option:
                0. Go back
                1. Process Pending Orders.
                """);
        choice = HelperMethod.input(0, 1);
        if (choice == -1) {
            return;
        }
        processPendingOrders();
    }

    private void refundOperations(ArrayList<Order> orders){
        int choice;
        System.out.println("""
                Choose an option:
                0. Go back
                1. Process Refunds.
                """);
        choice = HelperMethod.input(0, 1);
        if (choice == -1) {
            return;
        }
        processRefunds(orders);
    }

    private void displayOrderByStatus(Order.OrderStatus status){
        HelperMethod.printArrayList(getOrdersByStatus(status));
    }

    private ArrayList<Order> getOrdersByStatus(Order.OrderStatus... statuses) {
        ArrayList<Order> orders = new ArrayList<>();
        for (Order.OrderStatus status : statuses) {
            PriorityQueue<Order> queue = Main.getData().getOrdersByStatus().get(status);
            if (queue != null) {
                orders.addAll(queue);
            }
        }
        orders.sort(new CustomComparators.OrderComparator());
        return orders;
    }

    private void displayRemainingRefunds(Order.OrderStatus... statuses){
        HelperMethod.printArrayList(getRemainingRefunds(statuses));
    }

    private ArrayList<Order> getRemainingRefunds(Order.OrderStatus... statuses) {
        ArrayList<Order> remainingRefunds;

        if (statuses.length == 0) {
            remainingRefunds = getOrdersByStatus(Order.OrderStatus.DENIED, Order.OrderStatus.CANCELLED);
        } else {
            remainingRefunds = getOrdersByStatus(statuses);
        }

        remainingRefunds.removeIf(order -> order.getRefundStatus() == Order.RefundStatus.NA || order.getRefundStatus() == Order.RefundStatus.DONE);

        return remainingRefunds;
    }

    public void updateOrderStatus(Order order, Order.OrderStatus newStatus) {
        Main.getData().updateOrderStatus(order,newStatus);
    }

    private void processPendingOrders() {
        if(getOrdersByStatus(Order.OrderStatus.PENDING).isEmpty()){
            System.out.println("No pending order.");
            return;
        }
        for (Order order : getOrdersByStatus(Order.OrderStatus.PENDING)) {
            if (!order.getSpecialRequest().isEmpty()) {
                System.out.println("Special Request for Order ID " + order.getOrderID() + ": " + order.getSpecialRequest());
                System.out.print("Press Enter to continue...");
                Main.getScanner().nextLine();
            }
            updateOrderStatus(order, Order.OrderStatus.OUT_FOR_DELIVERY);
            System.out.println(order + " | Customer Type: " + order.getCustomer().getCustomerType());
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            markAsCompleted();
            scheduler.shutdown(); // Shut down the scheduler after the task is done
        }, 0, TimeUnit.SECONDS);
    }

    private void markAsCompleted(){
        if(getOrdersByStatus(Order.OrderStatus.OUT_FOR_DELIVERY).isEmpty()){
            return;
        }
        for(Order order : getOrdersByStatus(Order.OrderStatus.OUT_FOR_DELIVERY)){
            updateOrderStatus(order, Order.OrderStatus.COMPLETED);
            LocalDateTime endTime = LocalDateTime.now();
            if(Duration.between(Main.getTime(), endTime).getSeconds() >= 30){
                Main.updateTime();
                Main.canteen.getDailySale().setMoneyEarned(0);
                Main.canteen.getDailySale().setNumberOfOrders(0);
                Main.canteen.getDailySale().resetValue();
            }
            Main.canteen.getDailySale().increaseMoneyEarned(order.getTotalPrice());
            Main.canteen.getDailySale().increaseNumberOfOrders(1);
        }
    }

    private void processRefunds(ArrayList<Order> possibleRefunds){
        if(possibleRefunds.isEmpty()){
            System.out.println("No refunds pending.");
            return;
        }
        for(Order order : possibleRefunds){
            Customer customer = order.getCustomer();
            customer.getShopCard().increaseBalance(order.getTotalPrice());
            order.setRefundStatus(Order.RefundStatus.DONE);
            System.out.println(order + " | Customer Type: " + customer.getCustomerType());
        }
    }

    private boolean menuItemPresent(String menuName){
        for(String key : Main.canteen.getMenu().keySet()){
            if(key.contains(menuName)){
                return true;
            }
        }
        return false;
    }

    private MenuItem getItemAtIndex(int index){
        MenuItem[] menuItems = Main.canteen.getMenu().values().toArray(new MenuItem[0]);
        return menuItems[index-1];
    }

    private void viewGUI(){
        AdminGUIManager adminGUIManager = new AdminGUIManager(this,Main.canteen.getMenu(), Main.getData().getOrdersByStatus());
        adminGUIManager.show();
    }
}
