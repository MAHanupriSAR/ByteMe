package gui.admin;

import users.Admin;
import javax.swing.*;
import java.awt.*;

public class HomePage extends JPanel {
    private JButton switchToMenuView;
    private JButton switchToOrderHistoryView;

    public HomePage(Admin admin){
        setLayout(new BorderLayout());
        showAdminDetails(admin);
        setupButtons();
    }

    private void showAdminDetails(Admin admin){
        String adminInfo = "Hello, " + admin.getName() + " (" + admin.getEmail() + ")";
        JLabel adminDetailsLabel = new JLabel(adminInfo);
        adminDetailsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        adminDetailsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(adminDetailsLabel,BorderLayout.CENTER);
    }

    private void setupButtons(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        switchToMenuView = new JButton("View Menu");
        buttonPanel.add(switchToMenuView);

        switchToOrderHistoryView = new JButton("View Order History");
        buttonPanel.add(switchToOrderHistoryView);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JButton getSwitchToMenuView() {
        return switchToMenuView;
    }
    public JButton getSwitchToOrderHistoryView() {
        return switchToOrderHistoryView;
    }
}
