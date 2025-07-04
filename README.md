# ByteMe: Canteen Management System

ByteMe is a comprehensive Canteen Management System designed to streamline the process of ordering, managing, and tracking food orders in a canteen environment. The project features both Admin and Customer interfaces, supports menu browsing, order management, payment via shop card, and persistent data storage using serialization.

## Features

### For Customers
- **Browse Menu:** Search, filter by category, and sort menu items by price.
- **Cart Management:** Add, remove, and modify items in the cart.
- **Order Placement:** Checkout and pay using a shop card (default card number: `000`, initial balance: `10,000`).
- **Order History:** View past orders and their statuses.
- **Review System:** Add and view reviews for menu items.

### For Admins
- **Menu Management:** Add, update, and remove menu items.
- **Order Management:** View and process pending orders, prioritize VIP customers, and update order statuses (Pending, Out for Delivery, Completed, Denied, Cancelled).
- **Sales Tracking:** View daily sales statistics and popular items.

### General
- **User Authentication:** Separate login for customers and admins.
- **GUI:** Built with Java Swing for both Admin and Customer roles.
- **Persistent Storage:** Uses serialization to save and load data.

## Default Credentials

### Customers
- Email: `a`, Password: `a` (NORMAL)
- Email: `b`, Password: `b` (NORMAL)
- Email: `c`, Password: `c` (VIP)
- Email: `d`, Password: `d` (VIP)

### Admin
- Email: `ad`, Password: `ad`

## How to Run

1. **Clone the repository:**
   ```sh
   git clone https://github.com/MAHanupriSAR/ByteMe
   ```
2. **Open the project in your preferred Java IDE (e.g., IntelliJ IDEA, Eclipse).**
3. **Build and run the `Main` class located at `src/main/Main.java`.**
4. **Follow the on-screen instructions to log in as a customer or admin.**

## Project Structure

- `src/`
  - `canteenUtils/` — Core classes for menu items and orders
  - `exceptions/` — Custom exception classes
  - `gui/` — Swing GUI for admin and customer
  - `main/` — Entry point and data management
  - `test/` — JUnit tests
  - `users/` — User classes (Admin, Customer)
  - `utils/` — Utility classes (Cart, IO, Review, etc.)

## Technologies Used
- Java 8+
- Java Swing (GUI)
- JUnit (Testing)

## Notes
- All data is saved using Java serialization for persistence.
- Shop card details are hardcoded for demo purposes but can be changed in the code.
- The system prioritizes VIP customers when processing orders.

## Screenshots
> Add screenshots of the GUI here for better visualization.

## License
This project is licensed under the terms of the [MIT License](LICENSE).
