package client;

import client.Registration.UserRegistration;
import models.account.Account;
import models.account.Customer;
import models.account.Employee;
import models.account.Vendor;

import static ConsoleUI.CleanScreen.cleanScreen;
import static ConsoleUI.Getter.getText;

public class Menu {

    public static void startMenu() {
        while (true) {
            cleanScreen();
            String symbol = getText("""
                    RasDvaCRM\n
                    
                    Для продолжения работы выберите из меню:\
                    
                    [R|r]egistration\
                    
                    [L|l]ogin\
                    
                    [E|e]xit
                    """);

            switch (symbol.toLowerCase()) {
                case "r":
                    UserRegistration.registrationUser();
                case "l":
                    Account user = UserLogin.login();
                    generalMenu(user);
                    break;
                case "e":
                    return;
            }
        }
    }

    private static void generalMenu(Account user) {
        while (true) {
            cleanScreen();
            if (user instanceof Customer) {
            } else if (user instanceof Employee) {
            } else if (user instanceof Vendor) {
            }
        }
    }

}
