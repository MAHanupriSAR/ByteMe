package main;

import canteenUtils.Canteen;
import utils.IOHelper;
import utils.LoginManager;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    private static Data data = new Data(IOHelper.DATA_FILE);
    private static final Scanner scanner = new Scanner(System.in);
    public static final Canteen canteen = new Canteen();
    private static LocalDateTime time = LocalDateTime.now();

    public static Scanner getScanner() {
        return scanner;
    }

    private static void displayUserMenu(int userIndex, int userType){
        if(userType==1){
            data.getCustomers().get(userIndex).functionalities();
        }
        if(userType==2){
            data.getAdmins().get(userIndex).functionalities();
        }
    }

    public static void main(String[] args) {
        while(true){
            int infoAboutUser = LoginManager.run();
            if(infoAboutUser==-1){break;}

            int userType = infoAboutUser%10;
            int userIndex = infoAboutUser/10;

            displayUserMenu(userIndex,userType);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> data.saveData()));
    }

    public static LocalDateTime getTime() {
        return time;
    }

    public static void updateTime(){
        time = LocalDateTime.now();
    }

    public static Data getData() {
        return data;
    }

    public static void setData(Data data) {
        Main.data = data;
    }
}