package gui.customer;

import canteenUtils.Order;
import canteenUtils.MenuItem;
import users.Customer;
import utils.Cart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class CustomerGUIManager {
    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final HomePage homePage;
    private final MenuView menuView;
    private final OrderHistoryView orderHistoryView;
    private final CartView cartView;

    public CustomerGUIManager(Customer customer, Map<String, MenuItem> menu, Map<Order.OrderStatus, ArrayList<Order>> pastOrders, Cart cart) {
        frame = new JFrame("Customer GUI");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        homePage = new HomePage(customer);
        menuView = new MenuView(menu);
        orderHistoryView = new OrderHistoryView(pastOrders);
        cartView = new CartView(cart);

        cardPanel.add(homePage, "HomePage");
        cardPanel.add(menuView, "Menu");
        cardPanel.add(orderHistoryView, "Orders");
        cardPanel.add(cartView, "Cart");

        setUpButtons();

        frame.add(cardPanel);
    }

    private void setUpButtons(){
        setUpButtonsForHomePage();
        setUpButtonsForMenuView();
        setUpButtonsForOrderHistoryView();
        setUpButtonsForCartView();
    }

    private void setUpButtonsForHomePage(){
        homePage.getSwitchToMenuView().addActionListener(e->cardLayout.show(cardPanel,"Menu"));
        homePage.getSwitchToOrderHistoryView().addActionListener(e->cardLayout.show(cardPanel,"Orders"));
        homePage.getSwitchToCartView().addActionListener(e->cardLayout.show(cardPanel,"Cart"));
    }
    private void setUpButtonsForMenuView(){
        menuView.getBackButton().addActionListener(e -> cardLayout.show(cardPanel, "HomePage"));
    }
    private void setUpButtonsForOrderHistoryView(){
        orderHistoryView.getBackButton().addActionListener(e -> cardLayout.show(cardPanel, "HomePage"));
    }
    private void setUpButtonsForCartView(){
        cartView.getBackButton().addActionListener(e->cardLayout.show(cardPanel,"HomePage"));
    }


    public void show() {
        frame.setVisible(true);
    }
}

