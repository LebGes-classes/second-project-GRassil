package client.Registration;

import models.account.Account;
import models.account.Vendor;

public class VendorRegistration {
    static Vendor regVendor(Account account) {
        Vendor vendor = new Vendor(account); // Создаем объект Поставщика

        return vendor;
    }
}
