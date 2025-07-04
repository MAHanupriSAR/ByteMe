package utils;

import canteenUtils.MenuItem;
import canteenUtils.Order;
import users.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class IOHelper {
    public static final String CUSTOMER_FILE = "resources/customers.txt";
    public static final String ADMIN_FILE = "resources/admins.txt";
    public static final String DATA_FILE = "resources/data.ser";
    private static final String ORDER_HISTORY_DIR = "resources/OrderHistories/";
    private static final String CART_DIR = "resources/Cart/";

    public static int getTotalLines(String fileName) {
        int lineCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return lineCount;
    }
    public static String getLineFromFile(String fileName, int lineNumber){
        String line = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int currentLine = 1;

            while ((line = reader.readLine()) != null) {
                if (currentLine == lineNumber) {
                    return line;
                }
                currentLine++;
            }
        }
        catch (IOException e){
            System.out.println("Line number " + lineNumber + " not found in the file.");
        }
        return line;
    }

    public static class CustomerIO{
        public static String getName(int lineNumber){
            String line = getLineFromFile(CUSTOMER_FILE,lineNumber);
            String[] parts = line.split(",");
            return parts[0];
        }
        public static String getEmail(int lineNumber){
            String line = getLineFromFile(CUSTOMER_FILE,lineNumber);
            String[] parts = line.split(",");
            return parts[1];
        }
        public static String getPassword(int lineNumber){
            String line = getLineFromFile(CUSTOMER_FILE, lineNumber);
            String[] parts = line.split(",");
            return parts[2];
        }
        public static Customer.CustomerType getVipStatus(int lineNumber){
            String line = getLineFromFile(CUSTOMER_FILE,lineNumber);
            String[] parts = line.split(",");
            if(Objects.equals(parts[3], "VIP")){
                return Customer.CustomerType.VIP;
            }
            else{
                return Customer.CustomerType.NORMAL;
            }
        }
        public static void setCustomerType(int lineNumber, Customer.CustomerType type) {
            try {
                List<String> lines = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading the file: " + e.getMessage());
                    return;
                }
                if (lineNumber <= 0 || lineNumber > lines.size()) {
                    System.out.println("Error: Line number out of range. Total lines in file: " + lines.size());
                    return;
                }

                String[] parts = lines.get(lineNumber - 1).split(",");
                if (parts.length > 3) {
                    parts[3] = (type == Customer.CustomerType.VIP) ? "VIP" : "NORMAL";
                    lines.set(lineNumber - 1, String.join(",", parts));
                } else {
                    System.out.println("Error: Invalid line format at line " + lineNumber);
                    return;
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
                    for (String updatedLine : lines) {
                        writer.write(updatedLine);
                        writer.newLine();
                        writer.flush();
                    }
                } catch (IOException e) {
                    System.out.println("Error writing to the file: " + e.getMessage());
                }

            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }

    }
    public static class AdminIO{
        public static String getName(int lineNumber){
            String line = getLineFromFile(ADMIN_FILE,lineNumber);
            String[] parts = line.split(",");
            return parts[0];
        }
        public static String getEmail(int lineNumber){
            String line = getLineFromFile(ADMIN_FILE,lineNumber);
            String[] parts = line.split(",");
            return parts[1];
        }
        public static String getPassword(int lineNumber){
            String line = getLineFromFile(ADMIN_FILE, lineNumber);
            String[] parts = line.split(",");
            return parts[2];
        }
    }
    public static class OrderHistoryIO{
        public static void saveOrderToFile(Order order, Customer customer){
            String filename = ORDER_HISTORY_DIR+customer.getName()+"_history.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write("Order ID: " + order.getOrderID());
                writer.newLine();
                writer.write("Status: " + order.getStatus());
                writer.newLine();
                writer.write("Refund Status: " + order.getRefundStatus());
                writer.newLine();
                writer.write("Special Request: " + order.getSpecialRequest());
                writer.newLine();
                writer.write("Items:");
                writer.newLine();

                for (Map.Entry<MenuItem, Integer> entry : order.getItems().entrySet()) {
                    MenuItem item = entry.getKey();
                    int quantity = entry.getValue();
                    writer.write(String.format(" - %s x%d = ₹%d", item.getName(), quantity, item.getPrice() * quantity));
                    writer.newLine();
                }

                writer.write("Total Price: ₹" + order.getTotalPrice());
                writer.newLine();
                writer.write("============================================");
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.out.println("Failed to save order history: " + e.getMessage());
            }
        }
        public static void showOrderHistory(String userID) {
            String fileName = ORDER_HISTORY_DIR + userID + "_history.txt";

            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("No order history found for user: " + userID);
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("Failed to retrieve order history: " + e.getMessage());
            }
        }
    }
    public static class CartIO{
        public static void saveCartToFile(Cart cart, Customer customer) {
            String cartFileName = CART_DIR+customer.getName()+".txt";
            if (cart.isEmpty()) {
                File file = new File(cartFileName);
                if (file.exists()) {
                    if (!file.delete()) {
                        System.out.println("Failed to delete empty cart file: " + cartFileName);
                    }
                }
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(cartFileName))) {
                for (Map.Entry<MenuItem, Integer> entry : cart.getCartContents().entrySet()) {
                    MenuItem item = entry.getKey();
                    int quantity = entry.getValue();
                    writer.write(String.format("%s,%s,%d,%d%n", item.getName(), item.getCategory(), item.getPrice(), quantity));
                }
                writer.write("TotalItems:" + cart.getTotalItems() + "\n");
                writer.write("TotalPrice:" + cart.getTotalPrice() + "\n");
                writer.flush();
            } catch (IOException e) {
                System.out.println("Error saving cart to file: " + e.getMessage());
            }
        }

    }
}
