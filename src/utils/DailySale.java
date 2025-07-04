package utils;

import canteenUtils.Canteen;
import canteenUtils.MenuItem;
import main.Main;

import java.io.Serializable;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DailySale implements Serializable {
    private int moneyEarned;
    private int numberOfOrders;
    private int numberOfItemsSold;
    private PriorityQueue<MenuItem> popularItems;
    private final ScheduledExecutorService scheduler;

    public DailySale() {
        moneyEarned = 0;
        numberOfOrders = 0;
        numberOfItemsSold = 0;
        popularItems = new PriorityQueue<>(Comparator.comparingInt(MenuItem::getNumberOfBuyings).reversed());

        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void resetValue() {
        moneyEarned = 0;
        numberOfOrders = 0;
        popularItems.clear();
        for (MenuItem item : Main.canteen.getMenu().values()) {
            if (item.getNumberOfBuyings() > 0) {
                popularItems.add(item);
            }
        }
    }

    public int getMoneyEarned() {
        return moneyEarned;
    }
    public void setMoneyEarned(int moneyEarned) {
        this.moneyEarned = moneyEarned;
    }
    public void increaseMoneyEarned(int value){
        this.moneyEarned += value;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }
    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }
    public void increaseNumberOfOrders(int value){
        this.numberOfOrders += value;
    }

    public int getNumberOfItemsSold() {
        return numberOfItemsSold;
    }

    public void setNumberOfItemsSold(int numberOfItemsSold) {
        this.numberOfItemsSold = numberOfItemsSold;
    }
    public void increaseNumberOfItemsSold(int value){
        this.numberOfItemsSold += value;
    }

    public PriorityQueue<MenuItem> getPopularItems() {
        return popularItems;
    }

    public String toString() {
        if(numberOfOrders==0){
            return "No sales for today.";
        }

        String firstItemName = (popularItems.isEmpty()) ? "No items" : popularItems.peek().getName();
        return String.format("Report for today:\nTotal Money Earned: %d\nTotal Orders: %d\nMost Popular Item: %s",
                moneyEarned, numberOfOrders, firstItemName);
    }
}
