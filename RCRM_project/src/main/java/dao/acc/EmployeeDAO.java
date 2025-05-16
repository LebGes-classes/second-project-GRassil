package dao.acc;

import dao.DAO;
import dao.DBConnection;
import dao.storage.WarehouseDAO;
import models.account.Employee;
import models.account.EPosition;
import models.location.storage.wrh.Warehouse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO implements DAO<Employee> {
    AccountDAO accountDAO = new AccountDAO();
    WarehouseDAO warehouseDAO = new WarehouseDAO();

    @Override
    public int save(Employee account) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO user_employees (id, warehouse, position) VALUES (?, ?, ?)")) {

            // сначала сохраняем в таблицу users
            int id = accountDAO.save(account);

            // Теперь уже в user_employee
            stmt.setInt(1, id);
            stmt.setInt(2, account.getWarehouse().getId());
            stmt.setString(2, account.getPosition().toString());

            stmt.executeUpdate();

            return id;

        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return -1; // Возвращаем -1 в случае ошибки
        }
    }

    @Override
    public boolean update(Employee account) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE user_employees SET warehouse = ?, position = ? WHERE id = ?")) {

            stmt.setInt(1, account.getWarehouse().getId());
            stmt.setInt(2, account.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Возвращаем true, если обновление прошло успешно
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        return accountDAO.delete(id); // Удаляя из таблицы users, мы удалим и из user_employee
    }

    @Override
    public Employee getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT u.id,u.username,u.password,ue.warehouse " +
                     "FROM users u " +
                     "JOIN user_employees ue ON u.id = ue.id " +
                     "WHERE u.id = ?")) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs); // Преобразуем ResultSet в Employee
                } else {
                    System.out.println("Employee with ID " + id + " not found.");
                    return null;
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return null;
        }
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT u.id,u.username,u.password,ue.warehouse " +
                     "FROM users u join user_employees ue");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToEmployee(rs)); // Преобразуем ResultSet в Employee
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
        }

        return users;
    }

    // Вспомогательный метод для преобразования ResultSet в Employee
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        Warehouse wrh = warehouseDAO.getById(rs.getInt("warehouse"));
        EPosition EPosition = EPosition.valueOf(rs.getString("EPosition"));

        return new Employee(id, username, password, wrh, EPosition);
    }
}