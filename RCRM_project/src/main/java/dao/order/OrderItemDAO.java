package dao.order;

import dao.DAO;
import dao.DBConnection;
import models.order.Order;
import models.order.Product;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс OrderItemsDAO работает с таблицей order_items в базе данных.
 * Он отвечает за управление продуктами в заказах.
 */

public class OrderItemDAO {
    ProductDAO productDAO = new ProductDAO();

    /**
     * Сохраняет продукты для конкретного заказа в базу данных.
     * order - Заказ
     * Возвращает orderId, если сохранение прошло успешно, иначе false
     */
    public int save(Order order) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Начинаем транзакцию

            String insertOrderItemsSQL = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertOrderItemsSQL)) {
                for (Map.Entry<Product, Integer> entry : order.getOrderMap().entrySet()) {
                    stmt.setInt(1, order.getId());
                    stmt.setInt(2, entry.getKey().getId()); // ID продукта
                    stmt.setInt(3, entry.getValue()); // Количество
                    stmt.addBatch(); // Добавляем запрос в пакет
                }
                stmt.executeBatch(); // Выполняем все запросы из пакета
            }

            conn.commit(); // Завершаем транзакцию
            return order.getId();
        } catch (SQLException | IOException e) {
            System.out.println("Ошибка: " + e);
            return -1;
        }
    }



    /**
     * Получает все продукты для конкретного заказа.
     * orderId - ID заказа
     * Возвращает карта продуктов и их количества
     */
    public HashMap<Product, Integer> getByOrderId(int orderId) {
        HashMap<Product, Integer> items = new HashMap<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT product_id, quantity FROM order_items WHERE order_id = ?")) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product =  productDAO.getById(rs.getInt("product_id"));
                    int quantity = rs.getInt("quantity");
                    items.put(product, quantity);
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println("Ошибка: " + e);
        }

        return items;
    }
}