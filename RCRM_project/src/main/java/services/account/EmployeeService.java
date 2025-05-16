package services.account;

import dao.acc.EmployeeDAO;
import models.account.Employee;

public class EmployeeService {
    public static void save(Employee acc) {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        employeeDAO.save(acc);
    }
}
