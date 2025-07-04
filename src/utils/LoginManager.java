package utils;

import exceptions.EmptyInputException;
import exceptions.InvalidCredentialsException;
import main.Data;
import main.Main;
import users.Admin;
import users.Customer;

public class LoginManager {

    private static String takeEmailInput() throws EmptyInputException {
        System.out.print("Enter Email: ");
        String email = Main.getScanner().nextLine();
        if (email.isEmpty()) {
            throw new EmptyInputException("Email cannot be empty.");
        }
        return email;
    }

    private static String takePasswordInput() throws EmptyInputException {
        System.out.print("Enter Password: ");
        String password = Main.getScanner().nextLine();
        if (password.isEmpty()) {
            throw new EmptyInputException("Password cannot be empty.");
        }
        return password;
    }

    private static int login(int mode) throws EmptyInputException, InvalidCredentialsException {
        String email = takeEmailInput();
        String password = takePasswordInput();

        int success = -1;
        if (mode == 1) {
            success = Main.getData().customerPresent(email, password);
        } else if (mode == 2) {
            success = Main.getData().adminPresent(email, password);
        }

        if (success == -1) {
            throw new InvalidCredentialsException("Invalid credentials. Please try again.");
        }
        return (success * 10) + mode;
    }

    private static void signIn(int mode) throws EmptyInputException {
        System.out.print("Enter Name: ");
        String name = Main.getScanner().nextLine();
        String email = takeEmailInput();
        String password = takePasswordInput();

        if (mode == 1) {
            addNewCustomer(name, email, password);
        } else if (mode == 2) {
            addNewAdmin(name, email, password);
        }
        System.out.println("Account created successfully!\nProceed to login.");
    }

    private static void addNewCustomer(String name, String email, String password) {
        int totalLines = IOHelper.getTotalLines(IOHelper.CUSTOMER_FILE);
        Customer newCustomer = new Customer(name, email, password, Customer.CustomerType.NORMAL, totalLines+1);
        Main.getData().addCustomer(newCustomer);
    }

    private static void addNewAdmin(String name, String email, String password) {
        int totalLines = IOHelper.getTotalLines(IOHelper.ADMIN_FILE);
        Admin admin = new Admin(name, email, password, totalLines);
        Main.getData().addAdmin(admin);
    }

    private static int loginSigninOperations(int mode) {
        while (true) {
            try {
                System.out.println("""
                        Choose an option:
                        0. Go back
                        1. Login
                        2. SignUp
                        """);
                int choice = HelperMethod.input(0, 2);

                switch (choice) {
                    case 0 -> { return -2; }
                    case 1 -> { return login(mode); }
                    case 2 -> { signIn(mode); }
                }
            } catch (EmptyInputException | InvalidCredentialsException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static int run() {
        while (true) {
            System.out.println("""
                    Choose an option:
                    0. Exit
                    1. Continue as Customer
                    2. Continue as Admin
                    """);
            int choice = HelperMethod.input(0, 2);

            switch (choice) {
                case -1 -> { return -1; }
                case 1 -> { return loginSigninOperations(1); }
                case 2 -> { return loginSigninOperations(2); }
            }
        }
    }
}
