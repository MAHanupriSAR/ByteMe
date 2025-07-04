package canteenUtils;

import utils.DailySale;

import java.io.Serializable;
import java.util.TreeMap;

public class Canteen implements Serializable {;
    private TreeMap<String, MenuItem> Menu;
    private DailySale dailySale;
    private int totalItems;

    public Canteen(){
        Menu = new TreeMap<>();
        initSnacks();
        initBeverages();
        dailySale = new DailySale();
    }

    public void printMenu() {
        System.out.println("---- Menu ----");
        for (MenuItem item : Menu.values()) {
            System.out.println(item + "|Buyings: "+ item.getNumberOfBuyings());
        }
        System.out.println("--------------");
    }

    public TreeMap<String, MenuItem> getMenu() {
        return Menu;
    }

    public int getTotalItems() {
        return totalItems;
    }
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
    public void increaseTotalItems(int value){
        this.totalItems += value;
    }

    public DailySale getDailySale() {
        return dailySale;
    }
    public void showDailySale(){
        System.out.println(dailySale);
    }

    private void initSnacks(){
        Menu.put("Snack:Samosa", new MenuItem("Samosa", "Snack", 15, 4)); totalItems++;
        Menu.put("Snack:Bread Pakora", new MenuItem("Bread Pakora","Snack",25, 4)); totalItems++;
        Menu.put("Snack:Vada Pav", new MenuItem("Vada Pav", "Snack", 20, 4)); totalItems++;
    }

    private void initBeverages(){
        Menu.put("Beverages:Masala Chai", new MenuItem("Masala Chai","Beverages", 25, 4)); totalItems++;
        Menu.put("Beverages:Coffee", new MenuItem("Coffee","Beverages",30, 4)); totalItems++;
        Menu.put("Beverages:Lassi", new MenuItem("Lassi", "Beverages",40, 4)); totalItems++;
    }
}
