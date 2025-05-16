package client;

import ConsoleUI.Getter;
import models.account.Account;
import models.account.EAccType;
import services.account.AccountService;

import java.util.Scanner;

import static ConsoleUI.GetScanner.getScanner;

public class UserLogin {

    public static Account login() {
        Account account;
        do {
            EAccType accountType = Getter.getType();
            String username = Getter.getText("Enter username: ");
            String password = Getter.getText("Enter password: ");
            account = AccountService.login(accountType,username, password);
        } while(account!=null);
        return account;
    }
}

