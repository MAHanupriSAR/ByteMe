package utils;

import main.Main;

import java.util.*;

public class HelperMethod {
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str; // return the original string if it's null or empty
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static int input(int lowerBound, int upperBound){
        int choice;
        while (true){
            try {
                System.out.print("Input(0 to go back): ");
                choice = Main.getScanner().nextInt(); Main.getScanner().nextLine();
                if (choice == 0) {
                    return -1;
                } else if (lowerBound+1 <= choice && choice <= upperBound) {;
                    return choice;
                } else {
                    System.out.println("please enter a valid choice!");
                }
            }
            catch (InputMismatchException e){
                System.out.println("please enter a number!");
                Main.getScanner().nextLine();
            }
        }
    }

    public static ArrayList<Integer> multipleInputs(int lowerBound, int upperBound){
        while (true) {
            System.out.println("Enter unique numbers within the range " + lowerBound + " to " + upperBound + " (or press Enter to finish):");
            String input = Main.getScanner().nextLine();

            ArrayList<Integer> result = new ArrayList<>();

            if (input.isEmpty()) {
                return result;
            }

            String[] numbers = input.split("\\s+");
            HashSet<Integer> uniqueCheck = new HashSet<>();
            boolean allInRangeAndUnique = true;

            for (String numStr : numbers) {
                try {
                    int num = Integer.parseInt(numStr);

                    if (num < lowerBound || num > upperBound || !uniqueCheck.add(num)) {
                        allInRangeAndUnique = false;
                        break;
                    }
                    result.add(num);
                } catch (NumberFormatException e) {
                    allInRangeAndUnique = false;
                    break;
                }
            }

            if (allInRangeAndUnique) {
                return result;
            } else {
                System.out.println("Invalid input: all numbers must be unique and within range. Please try again.");
            }
        }
    }

    public static String generateRandomString(){
        String randomUUIDString = UUID.randomUUID().toString();
        String randomString = randomUUIDString.replace("-", "");
        return randomString.substring(0, 6);
    }

    public static int generateRandomNDigitNumber(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Number of digits must be at least 1");
        }
        Random random = new Random();
        // The range for n-digit numbers is 10^(n-1) to 10^n - 1
        int lowerBound = (int) Math.pow(10, n - 1);
        int upperBound = (int) Math.pow(10, n) - 1;
        return lowerBound + random.nextInt(upperBound - lowerBound + 1);
    }

    public static <T> void printArrayList(ArrayList<T> arrayList) {
        int count = 1;
        for (T obj : arrayList) {
            System.out.println(count + ". " + obj);
            count++;
        }
    }
}
