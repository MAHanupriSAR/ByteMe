package utils;

import canteenUtils.Order;
import users.Customer;

import javax.naming.directory.SearchResult;
import java.io.Serializable;
import java.util.Comparator;

public class CustomComparators implements Serializable {
    public static class OrderComparator implements Comparator<Order>, Serializable{
        @Override
        public int compare(Order o1, Order o2) {
            if (o1.getCustomer().getCustomerType() != o2.getCustomer().getCustomerType()) {
                return (o1.getCustomer().getCustomerType() == Customer.CustomerType.VIP) ? -1 : 1;
            }
            return Long.compare(o1.getTimeOfOrder(), o2.getTimeOfOrder());
        }
    }
}
