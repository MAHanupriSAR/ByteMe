package gui.admin;


import canteenUtils.MenuItem;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MenuView extends JPanel {
    private JButton backButton;

    public MenuView(Map<String, MenuItem> menu) {
        setLayout(new BorderLayout()); // Padding for better visuals
        setUpButton();
        showMenu(menu);
    }

    private void setUpButton(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        backButton = new JButton("Go Back");
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.NORTH);
    }

    private void showMenu(Map<String, MenuItem> menu){
        JPanel menuItems = new JPanel();
        menuItems.setLayout(new BoxLayout(menuItems, BoxLayout.Y_AXIS));

        int itemCount = 1;
        String category = "";
        for (Map.Entry<String, MenuItem> entry : menu.entrySet()) {
            String tempCategory = entry.getKey().split(":")[0];

            MenuItem menuItem = entry.getValue();
            if (!category.equals(tempCategory)) {
                category = tempCategory;
                String categoryText = "Category: " + category;
                JLabel categoryLabel = new JLabel(categoryText);
                categoryLabel.setForeground(Color.BLUE);
                menuItems.setFont(new Font("Arial", Font.BOLD,14));
                menuItems.add(categoryLabel);
            }

            String menuItemDetailLabelText = itemCount + ". " + menuItem;
            JLabel menuItemDetailLabel = new JLabel(menuItemDetailLabelText);
            menuItemDetailLabel.setForeground(Color.BLACK);
            menuItems.setFont(new Font("Arial", Font.PLAIN,14));
            menuItems.add(menuItemDetailLabel);
            itemCount++;
        }

        JScrollPane scrollPane = new JScrollPane(menuItems);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JButton getBackButton() {
        return backButton;
    }
}


