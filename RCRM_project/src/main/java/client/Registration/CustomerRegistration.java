package client.Registration;

import models.account.Account;
import models.account.Customer;
import models.location.storage.psm.Postamat;

import java.lang.reflect.InvocationTargetException;

import static ConsoleUI.Getter.getModel;

public class CustomerRegistration {
    //Регистрация покупателя
    static Customer regCustomer(Account account) {

        Customer customer = new Customer(account); // Создаем объект покупателя

        Postamat postamat = getModel(Postamat.class, "Enter your postamat id: "); // Получаем постамат

        customer.setPostamat(postamat); // Обновляем объект постамата

        return customer; // Возвращаем аккаунт

    }
}