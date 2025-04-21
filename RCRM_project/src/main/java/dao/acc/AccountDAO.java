package dao.acc;

import dao.DAO;
import dao.DBConnection;
import models.accounts.Account;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements DAO<Account> {

    // Сохраниение нового аккаунта в базу данных, и возврат id
    @Override
    public int save(Account account) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            // Устанавливаем значения параметров
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Возвращаем сгенерированный ID
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return -1; // Возвращаем -1 в случае ошибки
        }
    }

    // Обновление данных аккаунта в бд. Если обновление прошло успешно, возращает true, иначе false
    @Override
    public boolean update(Account account) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET username = ?, password = ? WHERE id = ?")) {

            // Устанавливаем значения параметров
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.setInt(3, account.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Возвращаем true, если обновление прошло успешно
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return false;
        }
    }

    // удаление аккаунта из бд
    @Override
    public boolean delete(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Возвращаем true, если удаление прошло успешно
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return false;
        }
    }

    // Получение данных аккаунта из бд по id
    @Override
    public Account getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs); // Преобразуем ResultSet в Account
                } else {
                    System.out.println("Account with ID " + id + " not found.");
                    return null;
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return null;
        }
    }

    // Получение всех аккаунтов из бд и возврат в виде списка
    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs)); // Преобразуем ResultSet в Account
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
        }

        return accounts;
    }

    // Вспомогательный метод для преобразования ResultSet в Account
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");

        return new Account(id, username, password);
    }
}