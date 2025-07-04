package gui.admin;

import canteenUtils.MenuItem;
import canteenUtils.Order;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;

public class OrderView extends JPanel {
    private JButton backButton;

    public OrderView(Map<Order.OrderStatus, PriorityQueue<Order>> ordersByStatus) {
        setLayout(new BorderLayout()); // Padding for better visuals
        setUpButton();
        showOrders(ordersByStatus);
    }

    private void setUpButton(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        backButton = new JButton("Go Back");
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.NORTH);
    }

    private void showOrders(Map<Order.OrderStatus, PriorityQueue<Order>> ordersByStatus){
        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));

        for(Map.Entry<Order.OrderStatus,PriorityQueue<Order>> entry : ordersByStatus.entrySet()){
            Order.OrderStatus status = entry.getKey();
            PriorityQueue<Order> orders = entry.getValue();

            JLabel statusLabel = new JLabel(status.toString());
            statusLabel.setForeground(Color.BLUE);
            ordersPanel.setFont(new Font("Arial", Font.BOLD,14));
            ordersPanel.add(statusLabel);

            for (Order order : orders) {
                String orderDetailsText = order.toString() + " | " + order.getCustomer().getCustomerType();
                JLabel orderLabel = new JLabel(orderDetailsText);
                orderLabel.setForeground(Color.BLACK);
                ordersPanel.setFont(new Font("Arial", Font.PLAIN,14));
                orderLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        // Define the action here (e.g., show order details)
                        JOptionPane.showMessageDialog(
                                OrderView.this,
                                getOrderDetails(order),
                                "Order Details",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }

                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        orderLabel.setForeground(Color.RED); // Optional: Highlight on hover
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        orderLabel.setForeground(Color.BLACK); // Reset color on exit
                    }
                });
                ordersPanel.add(orderLabel);
            }
        }
        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    private String getOrderDetails(Order order) {
        StringBuilder details = new StringBuilder();
        details.append("Items:\n");
        for (Map.Entry<MenuItem, Integer> itemEntry : order.getItems().entrySet()) {
            details.append(itemEntry.getKey().getName());
            details.append(" (x").append(itemEntry.getValue()).append(")\n");
        }

        details.append("Total Items: ").append(order.getTotalItems()).append("\n");
        details.append("Total Price: ₹").append(order.getTotalPrice()).append("\n");

        return details.toString();
    }

    public JButton getBackButton() {
        return backButton;
    }
}