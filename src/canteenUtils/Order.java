package canteenUtils;

import users.Customer;
import utils.Cart;
import utils.HelperMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Order implements Serializable {
    private Customer customer;
    private String orderID;
    private Map<MenuItem, Integer> items;
    private String specialRequest;
    private int totalPrice;
    private int totalItems;
    private RefundStatus refundStatus;
    private OrderStatus status;
    private long timeOfOrder;

    public enum OrderStatus{
        PENDING,
        OUT_FOR_DELIVERY,
        COMPLETED,
        DENIED,
        CANCELLED;
    }

    public enum RefundStatus{
        NA, DONE, PENDING;
    }

    public Order(Cart cart){
        this.orderID = HelperMethod.generateRandomString();
        transferItemsFromCart(cart);
        this.status = OrderStatus.PENDING;
        timeOfOrder = System.currentTimeMillis();
        specialRequest = "";
    }

    private void transferItemsFromCart(Cart cart){
        this.items = new HashMap<>();
        for(Map.Entry<MenuItem,Integer> entry : cart.getCartContents().entrySet()){
            MenuItem menuItem = new MenuItem(entry.getKey());
            items.put(menuItem,entry.getValue());
        }
        this.totalItems = cart.getTotalItems();
        this.totalPrice = cart.getTotalPrice();
    }

    public boolean containsItem(MenuItem menuItem){
        for(Map.Entry<MenuItem,Integer> entry : items.entrySet()){
            if(entry.getKey().equals(menuItem)){
                return true;
            }
        }
        return false;
    }

    public Map<MenuItem, Integer> getItems() {
        return items;
    }

    public void showItems(){
        for(Map.Entry<MenuItem,Integer> entry : items.entrySet()){
            MenuItem menuItem = entry.getKey();
            System.out.println(menuItem);
        }
    }



    public String getOrderID() {
        return orderID;
    }
    public int getTotalItems() {
        return totalItems;
    }
    public int getTotalPrice() {
        return totalPrice;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    public OrderStatus getStatus() {
        return this.status;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }
    public RefundStatus getRefundStatus() {
        return refundStatus;
    }

    public long getTimeOfOrder() {
        return timeOfOrder;
    }

    public void setSpecialRequest(String request) {
        this.specialRequest = request;
    }
    public String getSpecialRequest() {
        return specialRequest;
    }

    public String toString(){
        return "Order ID: " + orderID + " | Status: " + this.status + " | Refund: " + this.refundStatus;
    }
}
