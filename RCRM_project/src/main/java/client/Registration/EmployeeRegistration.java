package client.Registration;

import models.account.Account;
import models.account.Employee;
import models.location.storage.wrh.Warehouse;

import static ConsoleUI.Getter.*;

public class EmployeeRegistration {
    static Employee regEmployee(Account account) {
        Employee employee = new Employee(account); // Создаем объект работника

        employee.setPosition(getPosition()); // Регистрация должности
        employee.setWarehouse(getModel(Warehouse.class, "Enter warehouse id: ")); // Регистрация склада

        return employee;
    }
}