package utils;

import canteenUtils.MenuItem;
import exceptions.ItemNotAvailable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart implements Serializable{
    private Map<MenuItem, Integer> cart;
    private int totalPrice;
    private int totalItems;

    public Cart() {
        this.cart = new HashMap<>();
        this.totalPrice = 0;
    }

    public void addItem(MenuItem item, int quantity) throws ItemNotAvailable {
        if (item.stocksAvailable() == 0) {
            throw new ItemNotAvailable(item.getName() + " is out of stock.");
        }
        if (item.stocksAvailable() < quantity) {
            throw new ItemNotAvailable("Only " + item.stocksAvailable() + " " + item.getName() + " are available.");
        }
        item.decreaseStocks(quantity);
        cart.put(item, cart.getOrDefault(item, 0) + quantity);
        totalPrice += item.getPrice() * quantity;
        totalItems += 1;
    }


//    public void addItem(MenuItem item, int quantity) {
//        if(item.stocksAvailable()==0){
//            System.out.println(item.getName()+" is out of stock.");
//        }
//        if(item.stocksAvailable()<quantity){
//            System.out.println("Only "+item.stocksAvailable()+" "+item.getName()+" are available");
//            return;
//        }
//        item.decreaseStocks(quantity);
//        cart.put(item, cart.getOrDefault(item, 0) + quantity);
//        totalPrice += item.getPrice() * quantity;
//        totalItems += 1;
//    }

    public void recalculateTotalPrice() {
        totalPrice = 0;
        for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
            MenuItem menuItem = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += menuItem.getPrice() * quantity;
        }
    }

    public void modifyItemQuantity(MenuItem item, int newQuantity) {
        if(newQuantity<0){
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        int currentQuantity = cart.get(item);
        totalPrice -= currentQuantity * item.getPrice();

        if (newQuantity == 0) {
            cart.remove(item);
            totalItems -= 1;
        } else {
            cart.put(item, newQuantity);
            totalPrice += newQuantity * item.getPrice();
        }
    }

    public void modifyItemQuantity(int index, int newQuantity) {
        MenuItem item = new ArrayList<>(cart.keySet()).get(index);
        modifyItemQuantity(item, newQuantity);
    }


    public void removeItem(MenuItem item) {
        totalPrice -= item.getPrice() * cart.get(item);
        totalItems -= 1;
        cart.remove(item);
    }
    public void removeItem(int index){
        MenuItem item = new ArrayList<>(cart.keySet()).get(index);
        removeItem(item);
    }

    public void clearCart() {
        cart.clear();
        totalPrice = 0;
        totalItems = 0;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Map<MenuItem, Integer> getCartContents() {
        return new HashMap<>(cart);
    }

    public int getTotalItems() {
        return totalItems;
    }

    public boolean isEmpty(){
        return this.totalItems==0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Your cart:\n");
        int itemCount = 1;

        for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
            MenuItem menuItem = entry.getKey();
            int quantity = entry.getValue();
            sb.append(String.format("%d. %s x %d = ₹%d%n", itemCount, menuItem.getName(), quantity, menuItem.getPrice() * quantity));
            itemCount++;
        }

        sb.append("Total price: ₹").append(totalPrice);
        return sb.toString();
    }
}
