package client.Registration;

import ConsoleUI.Getter;
import models.account.*;
import services.account.CustomerService;
import services.account.EmployeeService;
import services.account.VendorService;

import static ConsoleUI.Printer.printMsg;
import static client.Registration.CustomerRegistration.regCustomer;
import static client.Registration.EmployeeRegistration.regEmployee;
import static client.Registration.VendorRegistration.regVendor;

public class UserRegistration {

    public static void registrationUser() {

        printMsg("REGISTRATION");

        String username = Getter.getApprovedText("username");// регистрация имени
        String password = Getter.getApprovedText("password"); // регистрация пароля

        Account defAccount = new Account(username, password); // Создаем дефолтный аккаунт

        EAccType eAccType = Getter.getType(); // регистрация типа аккаунта
        defAccount.setEAccType(eAccType);
        switch (eAccType) {
            // Если пользователь регистрируется как работник
            case EAccType.EMPLOYEE -> {
                Employee acc = regEmployee(defAccount); // Окно регистрации
                EmployeeService.save(acc); // Сохранение в бд
                printMsg("You registrated successfully. Your account:\n" + acc); // Показываем данные аккаунта
            }
            // Если пользователь регистрируется как поставщик
            case EAccType.VENDOR -> {
                Vendor acc = regVendor(defAccount); // Окно регистрации
                VendorService.save(acc); // Сохранение в бд
                printMsg("You registrated successfully. Your account:\n" + acc); // Показываем данные аккаунта
            }
            // Если пользователь регистрируется как покупатель
            case EAccType.CUSTOMER -> {
                Customer acc = regCustomer(defAccount); // Окно регистрации
                CustomerService.save(acc); // Сохранение в бд
                printMsg("You registrated successfully. Your account:\n" + acc); // Показываем данные аккаунта
            }
        }
    }
}
