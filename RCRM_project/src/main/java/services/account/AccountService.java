package services.account;


import dao.acc.AccountDAO;
import models.account.Account;

import java.util.List;

public class AccountService {

    public static List<Account> getAll(){
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.getAll();
    }

    public static Account login(String username, String password) {
        List<Account> allAccounts = getAll(); // Получаем все аккаунты

        // Проходимся по всем аккаунтам, ищем с подходящим именем и паролем
        for (Account account : allAccounts) {
            // Если нашли, то возвращаем аккаунт
            if (account.getUsername() != null && account.getUsername().equals(username) &&
            account.getPassword() != null && account.getPassword().equals(password)) {
                return account;
            }
        }

        // Если не нашли, то возвращаем null
        return null;
    }
}
