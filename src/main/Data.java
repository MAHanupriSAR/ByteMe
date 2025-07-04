package main;

import canteenUtils.Order;
import users.Admin;
import users.Customer;
import utils.CustomComparators;
import utils.IOHelper;

import java.io.*;
import java.util.*;


public class Data implements Serializable{
    private ArrayList<Admin> admins ;
    private ArrayList<Customer> customers;
    private Map<Order.OrderStatus, PriorityQueue<Order>> ordersByStatus;

    public Data(String filename){
        File file = new File(filename);
        if(file.exists()){
            loadData();
        }
        else {
            admins = new ArrayList<>();
            customers = new ArrayList<>();
            ordersByStatus = new HashMap<>();
            for (Order.OrderStatus status : Order.OrderStatus.values()) {
                ordersByStatus.put(status, new PriorityQueue<>(new CustomComparators.OrderComparator()));
            }
            loadCustomers();
            loadAdmins();
        }
    }
    
    public Data(){
        admins = new ArrayList<>();
        customers = new ArrayList<>();
        ordersByStatus = new HashMap<>();
        for (Order.OrderStatus status : Order.OrderStatus.values()) {
            ordersByStatus.put(status, new PriorityQueue<>(new CustomComparators.OrderComparator()));
        }
    }

    public void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(IOHelper.DATA_FILE))) {
            out.writeObject(this);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(IOHelper.DATA_FILE))) {
            Data loadedData = (Data) in.readObject();
            admins = loadedData.admins;
            customers = loadedData.customers;
            ordersByStatus = loadedData.ordersByStatus;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private void loadCustomers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(IOHelper.CUSTOMER_FILE))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String email = parts[1];
                String password = parts[2];
                Customer.CustomerType type = Customer.CustomerType.valueOf(parts[3]);
                Customer customer = new Customer(name, email, password, type, lineNumber);
                customers.add(customer);
                lineNumber++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadAdmins() {
        try (BufferedReader reader = new BufferedReader(new FileReader(IOHelper.ADMIN_FILE))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String email = parts[1];
                String password = parts[2];
                // Create a new Admin with the line number
                Admin admin = new Admin(name, email, password, lineNumber);
                admins.add(admin);
                lineNumber++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOrder(Order order) {
        ordersByStatus.get(order.getStatus()).add(order);
        //System.out.println("Order " + order.getOrderID() + " added with status " + order.getStatus());
    }

    public void updateOrderStatus(Order order, Order.OrderStatus newStatus) {
        PriorityQueue<Order> currentQueue = ordersByStatus.get(order.getStatus());
        if (currentQueue != null) {
            currentQueue.remove(order);
        }

        PriorityQueue<Order> newQueue = ordersByStatus.get(newStatus);
        if (newQueue != null) {
            newQueue.add(order);
        }

        Customer customer = order.getCustomer();
        if (customer != null) {
            customer.updateOrderStatus(order, newStatus);
        }

        order.setStatus(newStatus);

        if(newStatus == Order.OrderStatus.DENIED || newStatus== Order.OrderStatus.CANCELLED){
            order.setRefundStatus(Order.RefundStatus.PENDING);
        }
        //System.out.println("Order " + order.getOrderID() + " status updated to " + newStatus);
    }

    private void initialiseDefaultCustomers(){
        Customer customer1 = new Customer("a","a","a", Customer.CustomerType.NORMAL);
        Customer customer2 = new Customer("b","b","b", Customer.CustomerType.NORMAL);
        Customer customer3 = new Customer("c","c","c", Customer.CustomerType.VIP);
        Customer customer4 = new Customer("d","d","d", Customer.CustomerType.VIP);

        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        customers.add(customer4);
    }

    private void initialiseDefaultAdmins(){
        Admin admin1 = new Admin("ad","ad","ad");
        admins.add(admin1);
    }

    public int customerPresent(String email, String password) {
        for (int i = 0; i < customers.size(); i++) {
            boolean emailMatched = customers.get(i).getEmail().equals(email);
            boolean passwordMatched = customers.get(i).getPassword().equals(password);
            if (emailMatched && passwordMatched) {
                return i;
            }
        }
        return -1;
    }
    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomerToFile(customer);
    }
    public void saveCustomerToFile(Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(IOHelper.CUSTOMER_FILE, true))) {
            writer.write(customer.getName() + "," + customer.getEmail() + "," + customer.getPassword() + "," + customer.getCustomerType() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }

    public  ArrayList<Customer> getCustomers() {
        return customers;
    }

    public int adminPresent(String email, String password) {
        for (int i = 0; i < admins.size(); i++) {
            boolean emailMatched = admins.get(i).getEmail().equals(email);
            boolean passwordMatched = admins.get(i).getPassword().equals(password);
            if (emailMatched && passwordMatched) {
                return i;
            }
        }
        return -1;
    }
    public void addAdmin(Admin admin) {
        admins.add(admin);
        saveAdminToFile(admin);
    }
    public void saveAdminToFile(Admin admin) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(IOHelper.ADMIN_FILE, true))) {
            writer.write(admin.getName() + "," + admin.getEmail() + "," + admin.getPassword() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving admin: " + e.getMessage());
        }
    }
    public ArrayList<Admin> getAdmins() {
        return admins;
    }

    public Map<Order.OrderStatus, PriorityQueue<Order>> getOrdersByStatus() {
        return ordersByStatus;
    }
}