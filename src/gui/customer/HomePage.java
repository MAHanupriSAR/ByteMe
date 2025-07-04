package gui.customer;

import users.Customer;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JPanel {
    private JButton switchToMenuView;
    private JButton switchToOrderHistoryView;
    private JButton switchToCartView;

    public HomePage(Customer customer){
        setLayout(new BorderLayout());
        showCustomerDetails(customer);
        setupButtons();
    }

    private void showCustomerDetails(Customer customer){
        String customerInfo = "Hello, " + customer.getName() + " (" + customer.getEmail() + ")";
        JLabel customerDetailsLabel = new JLabel(customerInfo);
        customerDetailsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        customerDetailsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(customerDetailsLabel,BorderLayout.CENTER);
    }

    private void setupButtons(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        switchToMenuView = new JButton("View Menu");
        buttonPanel.add(switchToMenuView);

        switchToOrderHistoryView = new JButton("View Order History");
        buttonPanel.add(switchToOrderHistoryView);

        switchToCartView = new JButton("View Cart");
        buttonPanel.add(switchToCartView);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JButton getSwitchToMenuView() {
        return switchToMenuView;
    }
    public JButton getSwitchToOrderHistoryView() {
        return switchToOrderHistoryView;
    }
    public JButton getSwitchToCartView() {
        return switchToCartView;
    }
}
