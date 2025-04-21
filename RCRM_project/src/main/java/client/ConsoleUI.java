package client;

import models.accounts.Account;
import dao.DBConnection;

import java.sql.ResultSet;
import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;
    private Account account;

    public ConsoleUI() {
        scanner = new Scanner(System.in);

        while (true){
            cleanScreen();
            System.out.println("RasDvaCRM");
            System.out.println();
            System.out.println("Для продолжения работы выберите из меню:");
            System.out.println("[R|r]egistration");
            System.out.println("[L|l]ogin");
            System.out.println("[E|e]xit");

            String symbol = scanner.nextLine();
            switch (symbol){
                case "R":
                case "r":
                    account = registration();
                    break;
                case "L":
                case "l":
                    account = login();
                    break;
                case "E":
                case "e":
                    System.exit(0);
            }
        }

    }

    public Account registration() {

        String login;
        String username;
        String password;
        String position;


        do {
            System.out.println("Введите ваше имя");
            username = scanner.nextLine();
        } while (!aproveChoise(username));

        do {
            System.out.println("Введите ваш пароль");
            password = scanner.nextLine();
            System.out.println("Повторите пароль");
        } while (!scanner.nextLine().equals(password));

        boolean flag = false;
        while (!flag) {
            System.out.println("Выберите свою должность:");
            System.out.println("[C|c]ustomer");
            System.out.println("[E|e]mployee");
            System.out.println("[V|v]endor");
            System.out.println("[A|a]dmin");

            String positionChar = scanner.nextLine();
            switch (positionChar.toLowerCase()) {
                case "c":
                    position = "customer";
                    flag = aproveChoise(position);
                    break;
                case "e":
                    position = "employee";
                    flag = aproveChoise(position);
                    break;
                case "v":
                    position = "vendor";
                    flag = aproveChoise(position);
                    break;
                case "a":
                    position = "admin";
                    flag = aproveChoise(position);
                    break;
                default:
                    System.out.println("Неправильный ввод, повторите попытку");
            }
        }

        login = username + "_" + position.charAt(0) + "_" +

        return null;
    }


    public Account login() {
        System.out.println("Введите ваш логин");
        String login = scanner.nextLine();
        System.out.println("Введите ваш пароль");
        String password = scanner.nextLine();


        String execute = "SELECT * FROM users WHERE login == ? and password == ?";
        ResultSet resultSet = DBConnection.execute(execute);

        return null;
    }

    private boolean aproveChoise(Object obj){
        System.out.printf("Подтвердите выбор - %d", obj);
        System.out.println("[Y|y]es/ [N|n]o");
        switch (scanner.nextLine().toLowerCase()){
            case "y":
                return true;
            case "n":
                return false;
            default:
                System.out.println("Неверный ввод");
                return aproveChoise(obj);
        }
    }

    private static void cleanScreen(){
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}