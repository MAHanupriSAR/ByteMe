package gui.admin;

import canteenUtils.MenuItem;
import canteenUtils.Order;
import users.Admin;
import users.Customer;
import utils.Cart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;

public class AdminGUIManager {
    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final HomePage homePage;
    private final MenuView menuView;
    private final OrderView orderHistoryView;

    public AdminGUIManager(Admin admin, Map<String, MenuItem> menu, Map<Order.OrderStatus, PriorityQueue<Order>> ordersByStatus) {
        frame = new JFrame("Customer GUI");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        homePage = new HomePage(admin);
        menuView = new MenuView(menu);
        orderHistoryView = new OrderView(ordersByStatus);

        cardPanel.add(homePage, "HomePage");
        cardPanel.add(menuView, "Menu");
        cardPanel.add(orderHistoryView, "Orders");

        setUpButtons();

        frame.add(cardPanel);
    }

    private void setUpButtons(){
        setUpButtonsForHomePage();
        setUpButtonsForMenuView();
        setUpButtonsForOrderHistoryView();
    }

    private void setUpButtonsForHomePage(){
        homePage.getSwitchToMenuView().addActionListener(e->cardLayout.show(cardPanel,"Menu"));
        homePage.getSwitchToOrderHistoryView().addActionListener(e->cardLayout.show(cardPanel,"Orders"));
    }
    private void setUpButtonsForMenuView(){
        menuView.getBackButton().addActionListener(e -> cardLayout.show(cardPanel, "HomePage"));
    }
    private void setUpButtonsForOrderHistoryView(){
        orderHistoryView.getBackButton().addActionListener(e -> cardLayout.show(cardPanel, "HomePage"));
    }

    public void show() {
        frame.setVisible(true);
    }
}

