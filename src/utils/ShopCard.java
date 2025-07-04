package utils;

import main.Main;

import java.io.Serializable;

public class ShopCard implements Serializable {
    private int balance;
    private int cardNumber;

    public ShopCard(int balance, int cardNumber){
        this.balance = balance;
        this.cardNumber = cardNumber;
    }

    public boolean validateCard() {
        System.out.print("Enter card number (-1 to exit): ");
        int input;

        while (true) {
            input = Main.getScanner().nextInt();
            Main.getScanner().nextLine(); // Consume newline

            if (input == -1) {
                return false; // Exit if user inputs -1
            }

            if (input == this.cardNumber) {
                return true; // Correct card number entered
            }

            System.out.println("Wrong Credentials. Please try again (-1 to exit)");
        }
    }

    public boolean hasSufficientBalance(int totalAmountToPay) {
        if(this.balance<totalAmountToPay){
            System.out.println("Insufficient balance.");
        }
        return this.balance >= totalAmountToPay;
    }

    public boolean confirmPayment(){
        System.out.print("Do you want to complete the payment (yes/no): ");
        String choice = Main.getScanner().nextLine();
        return choice.equalsIgnoreCase("yes");
    }

    public void pay(int Amount){
        this.balance -= Amount;
    }

    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }
    public void increaseBalance(int value){
        this.balance += value;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String toString(){
        return ("Card Number: " + cardNumber + " | Balance: " + balance);
    }
}
