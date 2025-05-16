package dao.acc;

import dao.DAO;
import dao.DBConnection;
import models.account.Vendor;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendorDAO implements DAO<Vendor> {
    AccountDAO accountDAO = new AccountDAO();

    @Override
    public int save(Vendor account) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO user_vendors (id, profit) VALUES (?, ?)")) {

            // сначала сохраняем в таблицу users
            int id = accountDAO.save(account);

            // Теперь уже в user_vendor
            stmt.setInt(1, id);
            stmt.setInt(2, account.getProfit());

            stmt.executeUpdate();

            return id;

        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return -1; // Возвращаем -1 в случае ошибки
        }
    }

    @Override
    public boolean update(Vendor account) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE user_vendors SET profit = ? WHERE id = ?")) {

            stmt.setInt(1, account.getProfit());
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
        return accountDAO.delete(id); // Удаляя из таблицы users, мы удалим и из user_vendor
    }

    @Override
    public Vendor getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT u.id,u.username,u.password, uv.profit " +
                     "FROM users u " +
                     "JOIN user_vendors uv ON uv.id = u.id " +
                     "WHERE u.id = ?")) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVendor(rs); // Преобразуем ResultSet в Vendor
                } else {
                    System.out.println("Vendor with ID " + id + " not found.");
                    return null;
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return null;
        }
    }

    @Override
    public List<Vendor> getAll() {
        List<Vendor> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT u.id,u.username,u.password,uv.profit " +
                     "FROM users u join user_vendors uv");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToVendor(rs)); // Преобразуем ResultSet в Vendor
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
        }

        return users;
    }

    // Вспомогательный метод для преобразования ResultSet в Vendor
    private Vendor mapResultSetToVendor(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        int profit = rs.getInt("profit");

        return new Vendor(id, username, password, profit);
    }
}