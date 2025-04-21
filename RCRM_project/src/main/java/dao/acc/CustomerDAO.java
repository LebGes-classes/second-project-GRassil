package dao.acc;

import dao.DAO;
import dao.DBConnection;
import dao.storage.PostamatDAO;
import models.accounts.Customer;
import models.storage.psm.Postamat;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements DAO<Customer> {

    private final AccountDAO accountDAO = new AccountDAO(); // DAO для работы с общей таблицей аккаунтов
    private final PostamatDAO postamatDAO = new PostamatDAO(); // DAO для работы с постаматами

    /**
     * Сохраняет нового клиента в базу данных.
     * Сначала сохраняет общие данные аккаунта в таблицу users,
     * затем сохраняет специфические данные клиента в таблицу user_customers.
     */
    @Override
    public int save(Customer customer) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO user_customers (id, postamat) VALUES (?, ?)")) {

            // 1. Сохраняем общие данные аккаунта в таблицу users
            int id = accountDAO.save(customer); // Получаем ID созданного аккаунта

            // 2. Сохраняем специфические данные клиента в таблицу user_customers
            stmt.setInt(1, id); // ID клиента
            stmt.setInt(2, customer.getPostamat().getId()); // ID постамата

            stmt.executeUpdate();

            return id; // Возвращаем ID созданного клиента

        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return -1; // Возвращаем -1 в случае ошибки
        }
    }

    /**
     * Обновляет данные существующего клиента в базе данных.
     * Обновляются только специфические данные клиента в таблице user_customers.
     */
    @Override
    public boolean update(Customer customer) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE user_customers SET postamat = ? WHERE id = ?")) {

            stmt.setInt(1, customer.getPostamat().getId()); // Новый ID постамата
            stmt.setInt(2, customer.getId()); // ID клиента

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Возвращаем true, если обновление прошло успешно
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return false;
        }
    }

    /**
     * Удаляет клиента из базы данных.
     * Удаление происходит через AccountDAO, так как удаление из таблицы users автоматически удаляет запись из user_customers.
     */
    @Override
    public boolean delete(int id) {
        return accountDAO.delete(id); // Удаляем запись из таблицы users
    }

    /**
     * Получает клиента по его ID.
     * Выполняется JOIN между таблицами users и user_customers для получения полной информации о клиенте.
     */
    @Override
    public Customer getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT u.id, u.username, u.password, uc.postamat " +
                     "FROM users u " +
                     "JOIN user_customers uc ON uc.id = u.id " +
                     "WHERE u.id = ?")) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs); // Преобразуем ResultSet в объект Customer
                } else {
                    System.out.println("Customer with ID " + id + " not found.");
                    return null;
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return null;
        }
    }

    /**
     * Получает всех клиентов из базы данных.
     * Выполняется JOIN между таблицами users и user_customers для получения полной информации о всех клиентах.
     */
    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT u.id, u.username, u.password, uc.postamat " +
                     "FROM users u " +
                     "JOIN user_customers uc ON uc.id = u.id");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs)); // Преобразуем ResultSet в объект Customer
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
        }

        return customers; // Возвращаем список клиентов
    }

    /**
     * Преобразует ResultSet в объект Customer.
     * Используется для маппинга данных из базы данных в объект.
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        int id = rs.getInt("id"); // ID клиента
        String username = rs.getString("username"); // Логин клиента
        String password = rs.getString("password"); // Пароль клиента
        Postamat psm = postamatDAO.getById(rs.getInt("postamat")); // Загружаем постамат по его ID

        return new Customer(id, username, password, psm); // Создаем и возвращаем объект Customer
    }
}