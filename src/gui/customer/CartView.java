package gui.customer;

import canteenUtils.MenuItem;
import utils.Cart;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class CartView extends JPanel {
    private JButton backButton;

    public CartView(Cart cart){
        setLayout(new BorderLayout());
        setupButton();
        showCartItems(cart);
    }

    private void setupButton(){
        JPanel topPanel  = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        backButton = new JButton("Go Back");
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);
    }

    private void showCartItems(Cart cart){
        JPanel cartItems = new JPanel();
        cartItems.setLayout(new BoxLayout(cartItems,BoxLayout.Y_AXIS));

        int itemCount = 1;
        for (Map.Entry<canteenUtils.MenuItem, Integer> entry : cart.getCartContents().entrySet()) {
            MenuItem menuItem = entry.getKey();
            int quantity = entry.getValue();
            String text = String.format("%d. %s x %d = ₹%d%n", itemCount, menuItem.getName(), quantity, menuItem.getPrice() * quantity);
            JLabel label = new JLabel(text);
            cartItems.setFont(new Font("Arial", Font.PLAIN, 14));
            cartItems.add(label);
            itemCount++;
        }

        JScrollPane scrollPane = new JScrollPane(cartItems);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JButton getBackButton() {
        return backButton;
    }
}
