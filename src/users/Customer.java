package users;

import java.util.*;

import canteenUtils.MenuItem;
import canteenUtils.Order;
import exceptions.ItemNotAvailable;
import gui.customer.CustomerGUIManager;
import main.Main;
import utils.*;

public class Customer extends User{
    public enum CustomerType{
        VIP, NORMAL;
    }

    private CustomerType customerType;
    private Map<Order.OrderStatus, ArrayList<Order>> orderByStatus;
    private Cart cart;
    private ShopCard shopCard;

    public Customer(String name, String email, String password, CustomerType customerType) {
        super(name, email, password);
        this.customerType = customerType;
        this.orderByStatus = new HashMap<>();
        for (Order.OrderStatus status : Order.OrderStatus.values()) {
            orderByStatus.put(status, new ArrayList<>());
        }
        cart = new Cart();
        //shopCard = new ShopCard(0, HelperMethod.generateRandomNDigitNumber(6));
        shopCard = new ShopCard(10000,000);
    }

    public Customer(String name, String email, String password, CustomerType customerType, int lineNumber) {
        super(name, email, password, lineNumber);
        this.customerType = customerType;
        this.orderByStatus = new HashMap<>();
        for (Order.OrderStatus status : Order.OrderStatus.values()) {
            orderByStatus.put(status, new ArrayList<>());
        }
        cart = new Cart();
        //shopCard = new ShopCard(0, HelperMethod.generateRandomNDigitNumber(6));
        shopCard = new ShopCard(10000,000);
    }

    public void functionalities(){
        int choice;
        boolean loop = true;
        while(loop){
            System.out.println("""
            Choose an option:
            0. Log out
            1. Browse Menu
            2. View Cart
            3. Checkout
            4. Manage Orders
            5. Manage Shopping Card
            6. Become a VIP
            7. View GUI Interface
            """);
            choice = HelperMethod.input(0,7);
            switch (choice){
                case -1 -> loop=false;
                case 1 -> browseMenu();
                case 2 -> viewCart();
                case 3 -> checkout();
                case 4 -> orderManagement();
                case 5 -> manageShoppingCard();
                case 6 -> becomeVIP();
                case 7 -> viewGUI();
            }
        }
    }

    private void showItems() {
        int itemCount = 1;
        String category = "";
        for (Map.Entry<String, MenuItem> entry : Main.canteen.getMenu().entrySet()) {
            String tempCategory = entry.getKey().split(":")[0];
            ;
            MenuItem menuItem = entry.getValue();
            if (!category.equals(tempCategory)) {
                category = tempCategory;
                System.out.println("Category: " + category);
            }
            System.out.println(itemCount + ". " + menuItem);
            itemCount++;
        }
    }

    private ArrayList<MenuItem> returnCategoryItems(String categoryFilter) {
        ArrayList<MenuItem> categoryItems = new ArrayList<>();
        System.out.println("Items in category: " + categoryFilter);
        for (Map.Entry<String, MenuItem> entry : Main.canteen.getMenu().entrySet()) {
            String itemCategory = entry.getKey().split(":")[0];

            if (itemCategory.equalsIgnoreCase(categoryFilter)) {
                MenuItem menuItemOriginal = entry.getValue();
                MenuItem menuItemCopied = new MenuItem(menuItemOriginal);
                categoryItems.add(menuItemOriginal);
            }
        }
        return categoryItems;
    }

    private ArrayList<MenuItem> returnSortedAccToPrice() {
        ArrayList<MenuItem> items = new ArrayList<>(Main.canteen.getMenu().values());
//        for (MenuItem menuItem : Main.canteen.getMenu().values()) {
//            items.add(new MenuItem(menuItem));
//        }
        items.sort(Comparator.comparingInt(MenuItem::getPrice));
        return items;
    }

    private void browseMenu() {
        showItems();
        browseMenuHelper();
    }

    private void browseMenuHelper() {
        int choice;
        boolean loop = true;
        while (loop) {
            System.out.println("""
            Choose an option:
            0. Go to main menu
            1. Search by name
            2. Filter by category
            3. Sort by price
            4. Add an item to cart
            5. View Review
            6. Give Review
            """);

            choice = HelperMethod.input(0, 6);
            switch (choice) {
                case -1 -> loop = false;
                case 1 -> searchByName();
                case 2 -> filterByCategory();
                case 3 -> sortByPrice();
                case 4 -> addItemToCart();
                case 5 -> viewReview();
                case 6 -> giveReview();
            }
        }
    }

    private void searchByName() {
        System.out.print("Search: ");
        String input = Main.getScanner().nextLine();
        ArrayList<MenuItem> matched = matchedItems(input);
        if (matched.isEmpty()) {
            System.out.println("No matches found.");
            return;
        }

        System.out.println("Search results for " + input + ":");
        HelperMethod.printArrayList(matched);

        operations(matched);
    }

    private void filterByCategory() {
        Map<String, MenuItem> menu = Main.canteen.getMenu();
        ArrayList<String> categories = new ArrayList<>();

        for (String key : menu.keySet()) {
            String category = key.split(":")[0];
            if (!categories.contains(category)) {
                categories.add(category);
            }
        }
        System.out.println("Choose a category to filter by:");
        System.out.println("0. Go back");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
        int categoryChoice = HelperMethod.input(0, categories.size());
        if (categoryChoice == -1) {
            return;
        }
        String selectedCategory = categories.get(categoryChoice - 1);
        ArrayList<MenuItem> categoryItems = returnCategoryItems(selectedCategory);

        HelperMethod.printArrayList(categoryItems);
        operations(categoryItems);
    }


    private void sortByPrice() {
        System.out.println("Menu items sorted by price: ");
        ArrayList<MenuItem> itemSortedByPrice = returnSortedAccToPrice();
        HelperMethod.printArrayList(itemSortedByPrice);

        operations(itemSortedByPrice);
    }

    public void operations(ArrayList<MenuItem> matched) {
        int choice;
        boolean loop = true;
        while (loop) {
            System.out.println("""
                    Choose an option:
                    0. Go back
                    1. Add an item to cart
                    2. View Review
                    3. Give Review
                    """);
            choice = HelperMethod.input(0, 3);
            switch (choice) {
                case -1 -> {
                    loop = false;
                }
                case 1 -> addItemToCart(matched);
                case 2 -> viewReview(matched);
                case 3 -> giveReview(matched);
            }
        }
    }

    private ArrayList<MenuItem> matchedItems(String input) {
        ArrayList<MenuItem> matches = new ArrayList<>();
        input = input.toLowerCase();
        for (Map.Entry<String, MenuItem> entry : Main.canteen.getMenu().entrySet()) {
            String key = entry.getKey().toLowerCase();
            if (key.contains(input)) {
                MenuItem menuItemOriginal = entry.getValue();
                MenuItem menuItemCopied = new MenuItem(menuItemOriginal);
                matches.add(menuItemOriginal);
            }
        }
        return matches;
    }

    private void addItemToCart() {
        ArrayList<MenuItem> allItems = new ArrayList<>(Main.canteen.getMenu().values());
        addItemToCart(allItems);
    }

    private void addItemToCart(ArrayList<MenuItem> matched) {
        ;
        System.out.println("Choose a menu item:");
        int choice = HelperMethod.input(0, matched.size());
        if (choice == -1) {
            return;
        }
        MenuItem chosenItem = matched.get(choice - 1);
        System.out.print("enter quantity for "+chosenItem.getName()+": ");
        int quantity = Main.getScanner().nextInt();
        Main.getScanner().nextLine();

        try {
            cart.addItem(chosenItem, quantity);
            System.out.printf("%s x%d has been added to the cart%n", chosenItem.getName(), quantity);
            IOHelper.CartIO.saveCartToFile(this.cart, this);
        }
        catch (ItemNotAvailable e){
            System.out.println(e.getMessage());
        }
    }

    private void viewReview(ArrayList<MenuItem> matched) {
        int choice;
        HelperMethod.printArrayList(matched);
        System.out.println("choose an item to show review.");
        choice = HelperMethod.input(0, matched.size());
        if (choice == -1) {
            return;
        }

        MenuItem chosenItem = matched.get(choice - 1);
        chosenItem.showReviews();
    }

    private void viewReview() {
        ArrayList<MenuItem> allItems = new ArrayList<>(Main.canteen.getMenu().values());
        viewReview(allItems);
    }

    private void giveReview(){
        ArrayList<MenuItem> allItems = new ArrayList<>(Main.canteen.getMenu().values());
        giveReview(allItems);
    }

    private void giveReview(ArrayList<MenuItem> matched){
        int choice;
        HelperMethod.printArrayList(matched);
        System.out.println("choose an item to give review.");
        choice = HelperMethod.input(0, matched.size());
        if (choice == -1) {
            return;
        }

        MenuItem chosenItem = matched.get(choice - 1);
        giveReview(chosenItem);
    }

    private void giveReview(MenuItem menuItem) {
        System.out.println("Do you want to give a rating (Leave empty to skip): ");
        String ratingInput = Main.getScanner().nextLine();

        int rating = -1;
        if (!ratingInput.isEmpty()) {
            try {
                rating = Integer.parseInt(ratingInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Rating not recorded.");
            }
        }

        System.out.println("Do you want to give a description (Leave empty to skip): ");
        String description = Main.getScanner().nextLine();

        if (rating != -1 || !description.isEmpty()) {
            menuItem.addReview(rating, description); // Assuming there's an addReview method in MenuItem
            System.out.println("Review submitted.");
        } else {
            System.out.println("No review submitted.");
        }
    }


    private void viewCart() {
        if(cart.isEmpty()){
            System.out.println("Cart is empty!");
            return;
        }
        System.out.println(cart);
        cartOperations();
    }

    private void cartOperations() {
        int choice;
        boolean loop = true;
        while (loop) {
            System.out.println("""
                    Choose an option:
                    0. Go to Main Menu
                    1. Modify Item Quantity
                    2. Remove Item
                    3. Clear Cart
                    4. Proceed to checkout
                    """);
            choice = HelperMethod.input(0, 4);
            switch (choice) {
                case -1 -> loop = false;
                case 1 -> modifyItemQuantity();
                case 2 -> removeItem();
                case 3 -> clearCart();
                case 4 -> checkout();
            }
            IOHelper.CartIO.saveCartToFile(this.cart, this);
        }
    }

    private void modifyItemQuantity() {
        System.out.print("Enter item number you want to modify: ");
        int choice = HelperMethod.input(0, cart.getTotalItems());
        if (choice == -1) {
            return;
        }
        System.out.print("Enter new Quantity: ");
        int newQuantity = Main.getScanner().nextInt();
        Main.getScanner().nextLine();

        try {
            cart.modifyItemQuantity(choice - 1, newQuantity);
            System.out.println("Quantity updated. Your cart:");
            System.out.println(cart);
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    private void removeItem() {
        System.out.print("Enter item number you want to delete: ");
        int choice = HelperMethod.input(0, cart.getTotalItems());
        if (choice == -1) {
            return;
        }
        cart.removeItem(choice - 1);
    }

    private void clearCart() {
        cart.clearCart();
        IOHelper.CartIO.saveCartToFile(this.cart, this);
    }

    private void checkout() {
        if(cart.getTotalItems()==0){
            System.out.println("No items to buy. Please add an item.");
            return;
        }
        Order orderPlaced = new Order(cart);
        makeSpecialRequest(orderPlaced);
        if (!shopCard.validateCard()) {
            return;
        }
        if (!shopCard.hasSufficientBalance(cart.getTotalPrice())) {
            return;
        }
        if (!shopCard.confirmPayment()) {
            return;
        }
        shopCard.pay(orderPlaced.getTotalPrice());
        IOHelper.OrderHistoryIO.saveOrderToFile(orderPlaced, this);
        markBuyingsOfItems();
        clearCart();
        orderPlaced.setCustomer(this);
        addOrder(orderPlaced);
        Main.getData().addOrder(orderPlaced);
        System.out.println("Order has been Placed.\n" + "Order Details -> " + orderPlaced);
    }

    private void markBuyingsOfItems(){
        for(Map.Entry<MenuItem,Integer> entry : cart.getCartContents().entrySet()){
            MenuItem menuItem = entry.getKey();
            int purchased = entry.getValue();
            menuItem.increaseNumberOfBuyings(purchased);
        }
    }

    private void makeSpecialRequest(Order order) {
        System.out.print("Any special request for this order (or leave empty if none): ");
        String specialRequest = Main.getScanner().nextLine();

        if (!specialRequest.isEmpty()) {
            order.setSpecialRequest(specialRequest);
            System.out.println("Special request added.");
        } else {
            System.out.println("No special request added.");
        }
    }

    private void addOrder(Order order) {
        orderByStatus.get(order.getStatus()).add(order);
    }

    public void updateOrderStatus(Order order, Order.OrderStatus newStatus) {
        orderByStatus.get(order.getStatus()).remove(order);
        order.setStatus(newStatus);
        orderByStatus.get(newStatus).add(order);
    }

    private void orderManagement() {
        int choice;
        boolean loop = true;
        while (loop) {
            System.out.println("""
                    Choose an option:
                    0. Go to Main Menu
                    1. View Pending Orders
                    2. View Out for Delivery Orders
                    3. View Completed Orders
                    4. View Cancelled Orders
                    5. View Denied Orders
                    6. View All orders
                    """);
            choice = HelperMethod.input(0, 6);
            switch (choice) {
                case -1 -> loop = false;
                case 1 -> {
                    displayOrderByStatus(Order.OrderStatus.PENDING);
                    orderOptions(getOrdersByStatus(Order.OrderStatus.PENDING));
                }
                case 2 -> {
                    displayOrderByStatus(Order.OrderStatus.OUT_FOR_DELIVERY);
                    orderOptions(getOrdersByStatus(Order.OrderStatus.OUT_FOR_DELIVERY));
                }
                case 3 -> {
                    displayOrderByStatus(Order.OrderStatus.COMPLETED);
                    orderOperationForCancelledCompleted(getOrdersByStatus(Order.OrderStatus.COMPLETED));
                }
                case 4 -> {
                    displayOrderByStatus(Order.OrderStatus.CANCELLED);
                    orderOperationForCancelledCompleted(getOrdersByStatus(Order.OrderStatus.CANCELLED));
                }
                case 5 -> displayOrderByStatus(Order.OrderStatus.DENIED);
                case 6 -> {
                    displayOrderByStatus();
                    orderOptions(getOrdersByStatus());
                }
            }
        }
    }

    private void orderOptions(ArrayList<Order> possibleCancellations) {
        int choice;
        System.out.println("""
                Choose an option:
                0. Go back
                1. Cancel a order
                """);
        choice = HelperMethod.input(0, 1);
        if (choice == -1) {
            return;
        }
        cancelOrder(possibleCancellations);
    }

    private void orderOperationForCancelledCompleted(ArrayList<Order> possibleReorders) {
        int choice;
        System.out.println("""
                Choose an option:
                0. Go back
                1. Reorder an Item
                """);
        choice = HelperMethod.input(0, 1);
        if (choice == -1) {
            return;
        }
        reorder(possibleReorders);
    }

    private void generalOrderOperations(ArrayList<Order> orders) {
        int choice;
        System.out.println("""
                Choose an option:
                0. Go back
                1. Reorder an Item
                2. Cancel an Order
                """);
        choice = HelperMethod.input(0, 1);
        switch (choice) {
            case -1 -> {
                return;
            }
            case 1 -> reorder(orders);
            case 2 -> cancelOrder(orders);
        }
    }

    public ArrayList<Order> getOrdersByStatus(Order.OrderStatus... statuses) {
        ArrayList<Order> resultOrders = new ArrayList<>();
        if (statuses.length == 0) {
            for (ArrayList<Order> orders : orderByStatus.values()) {
                resultOrders.addAll(orders);
            }
        } else {
            for (Order.OrderStatus status : statuses) {
                ArrayList<Order> orders = orderByStatus.get(status);
                if (orders != null) {
                    resultOrders.addAll(orders);
                }
            }
        }

        return resultOrders;
    }


    private void displayOrderByStatus(Order.OrderStatus... statuses) {
        if (statuses.length == 0) {
            System.out.println("Displaying all orders:");
        } else {
            System.out.print("Displaying orders with status: ");
            for (Order.OrderStatus status : statuses) {
                System.out.print(status + " ");
            }
            System.out.println();
        }
        HelperMethod.printArrayList(getOrdersByStatus(statuses));
    }

    private void cancelOrder(ArrayList<Order> possibleCancellations) {
        int choice = HelperMethod.input(0, possibleCancellations.size());
        if (choice == -1) {
            return;
        }

        Order toBeCancelled = possibleCancellations.get(choice - 1);

        if (toBeCancelled.getStatus() == Order.OrderStatus.COMPLETED || toBeCancelled.getStatus() == Order.OrderStatus.DENIED || toBeCancelled.getStatus()== Order.OrderStatus.CANCELLED){
            System.out.println("This order cant be cancelled due to its status.");
            return;
        }

        Main.getData().updateOrderStatus(toBeCancelled, Order.OrderStatus.CANCELLED);
        System.out.println("Order has been cancelled.");
    }

    private void reorder(ArrayList<Order> possibleReorders) {
        int choice = HelperMethod.input(0, possibleReorders.size());
        if (choice == -1) {
            return;
        }

        createReorder(possibleReorders.get(choice - 1));

        checkout();
    }
    private void createReorder(Order originalOrder) {
        for (Map.Entry<MenuItem, Integer> entry : originalOrder.getItems().entrySet()) {
            MenuItem currentMenuItem = Main.canteen.getMenu().get(entry.getKey().getCategory() + ":" + entry.getKey().getName());

            if (currentMenuItem == null) {
                System.out.println("Item " + entry.getKey().getName() + " is no longer available.");
                continue;
            }
            int quantity = entry.getValue();

            try {
                this.cart.addItem(currentMenuItem, quantity);
            }
            catch (ItemNotAvailable e){
                System.out.println(e.getMessage());
            }
        }
    }


    private void manageShoppingCard(){
        int choice;
        boolean loop = true;
        while(loop){
            System.out.println("""
            Choose an option:
            0. Go back
            1. Recharge
            2. View Card
            """);
            choice = HelperMethod.input(0,2);
            switch (choice){
                case -1 -> loop = false;
                case 1 -> rechargeCard();
                case 2 -> viewCardDetails();
            }
        }
    }

    private void rechargeCard() {
        System.out.print("Enter the amount(₹): ");
        int amount = Main.getScanner().nextInt(); Main.getScanner().nextLine();
        shopCard.increaseBalance(amount);
        System.out.println("Recharge of ₹"+amount+" has been done.");
    }

    private void becomeVIP(){
        if(customerType==CustomerType.VIP){
            System.out.println("You are already a VIP.");
            return;
        }
        int choice;
        System.out.println("""
        Choose an option:
        0. Go back
        1. Proceed to pay ₹10,000
        """);
        choice = HelperMethod.input(0,1);
        switch (choice){
            case -1 -> {return;}
            case 1 -> updateToVIP();
        }
    }

    private void updateToVIP(){
        if(this.customerType==CustomerType.VIP){
            System.out.println("You are already a VIP.");
        }
        checkoutForVIPPayment();
    }

    private void checkoutForVIPPayment() {
        if(!shopCard.validateCard()){return;}
        if(!shopCard.hasSufficientBalance(10000)){return;}
        if (!shopCard.confirmPayment()) {return;}

        shopCard.pay(10000);

        this.setCustomerType(CustomerType.VIP);
        IOHelper.CustomerIO.setCustomerType(this.getLineNumber(),CustomerType.VIP);

        System.out.println("You are now a VIP.");
    }

    private void viewGUI(){
        CustomerGUIManager customerGUIManager = new CustomerGUIManager(this, Main.canteen.getMenu(), orderByStatus, this.cart);
        customerGUIManager.show();
    }

    private void viewCardDetails() {
        System.out.println(shopCard);
    }

    public ShopCard getShopCard() {
        return shopCard;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }
    public CustomerType getCustomerType() {
        return customerType;
    }

    public Cart getCart() {
        return cart;
    }
}