package test;

import canteenUtils.Canteen;
import canteenUtils.MenuItem;
import exceptions.ItemNotAvailable;
import main.Data;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import users.Admin;
import users.Customer;

public class JunitTesting {
    private Data data;
    private Canteen canteen;

    public JunitTesting(){
        initData();
        initCanteen();
    }
    private void initData(){
        data = new Data();
        Customer customer = new Customer("Anuraag", "a@gmail.com", "abcd", Customer.CustomerType.NORMAL);
        data.addCustomer(customer);

        Admin admin = new Admin("admin", "iiitd@ac.in","1234");
        data.addAdmin(admin);
    }
    private void initCanteen(){
        canteen = new Canteen();
        canteen.getMenu().clear();
        canteen.getMenu().put("Snack:Bread Pakora", new MenuItem("Bread Pakora","Snack",25, 0));
        canteen.getMenu().put("Snack:Samosa", new MenuItem("Samosa", "Snack", 15, 5));
        canteen.getMenu().put("Snack:Vada Pav", new MenuItem("Vada Pav", "Snack", 20, 30));
    }

    @Test
    public void testOrderOutOfStockItem(){
        Customer customer = data.getCustomers().get(0);
        MenuItem itemInStock = canteen.getMenu().get("Snack:Samosa");
        MenuItem outOfStockItem = canteen.getMenu().get("Snack:Bread Pakora");
        Assertions.assertThrows(ItemNotAvailable.class, ()->{
            customer.getCart().addItem(outOfStockItem,1);
        });
        Assertions.assertThrows(ItemNotAvailable.class, ()->{
            customer.getCart().addItem(itemInStock,10);
        });
        Assertions.assertDoesNotThrow(()->{
            customer.getCart().addItem(itemInStock, 5);
        });
    }

    @Test
    public void testCartOperations(){
        Customer customer = data.getCustomers().get(0);
        MenuItem item = canteen.getMenu().get("Snack:Vada Pav");

        //add normally
        try {
            customer.getCart().addItem(item, 3);
            int expectedTotalPriceAfterAdding = item.getPrice() * 3;
            Assertions.assertEquals(expectedTotalPriceAfterAdding,customer.getCart().getTotalPrice());
        }
        catch(ItemNotAvailable e){
            System.out.println(e.getMessage());
        }

        //increase
        try{
            customer.getCart().modifyItemQuantity(item, 5);
            int expectedTotalPriceAfterAdding = item.getPrice() * 5;
            Assertions.assertEquals(expectedTotalPriceAfterAdding,customer.getCart().getTotalPrice());
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        //decrease
        try{
            customer.getCart().modifyItemQuantity(item, 2);
            int expectedTotalPriceAfterAdding = item.getPrice() * 2;
            Assertions.assertEquals(expectedTotalPriceAfterAdding,customer.getCart().getTotalPrice());
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        //set it to zero
        try{
            customer.getCart().modifyItemQuantity(item, 0);
            int expectedTotalPriceAfterAdding = 0;
            Assertions.assertEquals(expectedTotalPriceAfterAdding,customer.getCart().getTotalPrice());
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        //set it to -ve
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            customer.getCart().modifyItemQuantity(item, -1);
        });
    }


}