package services.account;

import dao.acc.CustomerDAO;
import models.account.Customer;

public class CustomerService {

    public static void save(Customer acc) {
        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.save(acc);
    }

}
