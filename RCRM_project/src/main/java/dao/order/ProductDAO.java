package dao.order;

import dao.DAO;
import dao.DBConnection;
import dao.acc.VendorDAO;
import dao.storage.StorageDAO;
import models.account.Vendor;
import models.order.Category;
import models.order.Product;
import models.location.storage.Storage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements DAO<Product> {

    @Override
    public int save(Product product) {
        String sql = "INSERT INTO products (name, description, category, price, storage_id, vendor_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Заполняем запрос данными
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setString(3, product.getCategory());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getStorage().getId());
            stmt.setInt(6, product.getVendor().getId());

            stmt.executeUpdate(); // Выполняем запрос

            // Получаем сгенерированный ключ (ID)
            int generatedId;
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1); // Берем первый столбец из результата (ID)
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }
            return generatedId;
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return -1;
        }
    }

    @Override
    public boolean update(Product product) {
        String sql = "UPDATE products SET " +
                "name = ?" +
                "description = ?" +
                "category = ?" +
                "price = ?" +
                "storage_id = ?" +
                "vendor_id = ?" +
                "WHERE  id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getStorage().getId());
            stmt.setInt(6, product.getVendor().getId());
            stmt.setInt(7, product.getId());

            stmt.executeUpdate();

            return true;
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return false;
        }
    }



    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Устанавливаем значение параметра для SQL-запроса
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            // Если была удалена хотя бы одна строка, возвращаем true
            return rowsAffected > 0;
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return false; // В случае ошибки возвращаем false
        }
    }

    @Override
    public Product getById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Устанавливаем значение параметра для SQL-запроса
            stmt.setInt(1, id);

            // Выполняем запрос и получаем результат
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Создаем объект Product и заполняем его данными из ResultSet, возвращаем объект
                    return convertResultSetToProduct(rs);
                } else {
                    // Если запись не найдена, возвращаем null
                    System.out.println("Product with ID " + id + " not found.");
                    return null;
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return null; // В случае ошибки возвращаем null
        }
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery()) {
            // Обрабатываем результат запроса
            while (rs.next()) {
                Product product = convertResultSetToProduct(rs);
                products.add(product); // Добавляем продукт в список
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
        }

        return products; // Возвращаем список товаров
    }

    private Product convertResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("price")
        );

        int storageId = rs.getInt("storage_id");
        int vendorId = rs.getInt("vendor_id");

        VendorDAO vendorDAO = new VendorDAO();
        StorageDAO storageDAO = new StorageDAO();

        Storage storage = storageDAO.getById(storageId);
        Vendor vendor = vendorDAO.getById(vendorId);

        Category category = Category.valueOf(rs.getString("category"));

        product.setCategory(category);
        product.setStorage(storage);
        product.setVendor(vendor);

        return product;
    }
}
