package dao.order;

import dao.DAO;
import dao.DBConnection;
import dao.acc.CustomerDAO;
import dao.acc.EmployeeDAO;
import dao.storage.StorageDAO;
import models.account.Customer;
import models.account.Employee;
import models.order.Order;
import models.order.Product;
import models.location.storage.Storage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDAO implements DAO<Order> {
    CustomerDAO customerDAO = new CustomerDAO();
    StorageDAO storageDAO = new StorageDAO();
    OrderItemDAO orderItemDAO = new OrderItemDAO();
    EmployeeDAO employeeDAO = new EmployeeDAO();

    /**
     * Сохраняет заказ в базу данных.
     * Сначала сохраняет основную информацию в таблицу orders,
     * затем сохраняет продукты в таблицу order_items.
     *
     * @param order объект Order, который нужно сохранить
     * @return ID созданного заказа или -1 в случае ошибки
     */
    @Override
    public int save(Order order) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Начинаем транзакцию

            // Сохраняем основную информацию о заказе в таблицу orders
            String insertOrderSQL = "INSERT INTO orders (customer_id, employee_id, storage_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, order.getCustomer().getId());
                stmt.setObject(2, order.getEmp() != null ? order.getEmp().getId() : null); // Может быть NULL
                stmt.setObject(3, order.getStorage() != null ? order.getStorage().getId() : null); // Может быть NULL

                stmt.executeUpdate();

                // Получаем сгенерированный ID заказа
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);

                        // Сохраняем продукты в таблицу order_items. Сначала через addBatch копим запросы, а потом разом выполняем через executeBatch
                        String insertOrderItemsSQL = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
                        try (PreparedStatement itemStmt = conn.prepareStatement(insertOrderItemsSQL)) {
                            for (var entry : order.getOrderMap().entrySet()) {
                                itemStmt.setInt(1, orderId);
                                itemStmt.setInt(2, entry.getKey().getId());
                                itemStmt.setInt(3, entry.getValue());
                                itemStmt.addBatch();
                            }
                            itemStmt.executeBatch();
                        }

                        conn.commit(); // Завершаем транзакцию
                        return orderId;
                    } else {
                        throw new SQLException("Ошибка создания заказа");
                    }
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return -1; // Возвращаем -1 в случае ошибки
        }
    }

    /**
     * Обновляет данные существующего заказа.
     * Обновляются только основные данные заказа (например, сотрудник или склад).
     *
     * @param order объект Order с обновленными данными
     * @return true, если обновление прошло успешно, иначе false
     */
    @Override
    public boolean update(Order order) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE orders SET employee_id = ?, storage_id = ? WHERE id = ?")) {

            stmt.setObject(1, order.getEmp() != null ? order.getEmp().getId() : null); // Может быть NULL
            stmt.setObject(2, order.getStorage() != null ? order.getStorage().getId() : null); // Может быть NULL
            stmt.setInt(3, order.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Возвращаем true, если обновление прошло успешно
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return false;
        }
    }

    /**
     * Удаляет заказ из базы данных по его ID.
     *
     * @param id ID заказа, который нужно удалить
     * @return true, если удаление прошло успешно, иначе false
     */
    @Override
    public boolean delete(int id) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Начинаем транзакцию

            // 1. Удаляем продукты из таблицы order_items
            try (PreparedStatement itemStmt = conn.prepareStatement("DELETE FROM order_items WHERE order_id = ?")) {
                itemStmt.setInt(1, id);
                itemStmt.executeUpdate();
            }

            // 2. Удаляем заказ из таблицы orders
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM orders WHERE id = ?")) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();

                conn.commit(); // Завершаем транзакцию
                return rowsAffected > 0;
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return false;
        }
    }

    /**
     * Получает заказ по его ID.
     *
     * @param id ID заказа, который нужно найти
     * @return объект Order или null, если заказ не найден
     */
    @Override
    public Order getById(int id) {


        try (Connection conn = DBConnection.getConnection()) {
            // 1. Получаем основную информацию о заказе
            String selectOrderSQL = "SELECT * FROM orders WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(selectOrderSQL)) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Customer customer = customerDAO.getById(rs.getInt("customer_id"));
                        Employee employee = rs.getObject("employee_id") != null ? employeeDAO.getById(rs.getInt("employee_id")) : null;
                        Storage storage = rs.getObject("storage_id") != null ? storageDAO.getById(rs.getInt("storage_id")) : null;

                        // 2. Получаем продукты из таблицы order_items
                        HashMap<Product, Integer> orderMap = orderItemDAO.getByOrderId(id);

                        return new Order(id, orderMap, storage, employee, customer);
                    } else {
                        System.out.println("Order with ID " + id + " not found.");
                        return null;
                    }
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error - " + e);
            return null;
        }
    }

    /**
     * Получает все заказы из базы данных.
     */
    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders");
                    ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int orderId = rs.getInt("id");
                Customer customer = customerDAO.getById(rs.getInt("customer_id"));
                Employee employee = rs.getObject("employee_id") != null ? employeeDAO.getById(rs.getInt("employee_id")) : null;
                Storage storage = rs.getObject("storage_id") != null ? storageDAO.getById(rs.getInt("storage_id")) : null;

                HashMap<Product, Integer> orderMap = orderItemDAO.getByOrderId(orderId);

                orders.add(new Order(orderId, orderMap, storage, employee, customer));
            }
        } catch (SQLException | IOException e) {
            System.out.println("Ошибка: " + e);
        }
        return orders;
    }

    /**
     * Получает все заказы из базы данных по id предприятия
     */
    public List<Order> getAllByStorageId(int storageId) {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders WHERE storage_id = ?");) {

            stmt.setInt(1,storageId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("id");
                Customer customer = customerDAO.getById(rs.getInt("customer_id"));
                Employee employee = rs.getObject("employee_id") != null ? employeeDAO.getById(rs.getInt("employee_id")) : null;
                Storage storage = rs.getObject("storage_id") != null ? storageDAO.getById(rs.getInt("storage_id")) : null;

                HashMap<Product, Integer> orderMap = orderItemDAO.getByOrderId(orderId);

                orders.add(new Order(orderId, orderMap, storage, employee, customer));
            }
        } catch (SQLException | IOException e) {
            System.out.println("Ошибка: " + e);
        }
        return orders;
    }

    /**
     * Получает все заказы из базы данных по id пользователя
     */
    public List<Order> getAllByCustomerId(int customerId) {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders WHERE customer_id = ?");) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("id");
                Customer customer = customerDAO.getById(rs.getInt("customer_id"));
                Employee employee = rs.getObject("employee_id") != null ? employeeDAO.getById(rs.getInt("employee_id")) : null;
                Storage storage = rs.getObject("storage_id") != null ? storageDAO.getById(rs.getInt("storage_id")) : null;

                HashMap<Product, Integer> orderMap = orderItemDAO.getByOrderId(orderId);

                orders.add(new Order(orderId, orderMap, storage, employee, customer));
            }
        } catch (SQLException | IOException e) {
            System.out.println("Ошибка: " + e);
        }
        return orders;
    }

    /**
     * Получает все заказы из базы данных по id пользователя
     */
    public List<Order> getAllByEmployeeId(int employeeId) {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders WHERE employee_id = ?");) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("id");
                Customer customer = customerDAO.getById(rs.getInt("customer_id"));
                Employee employee = rs.getObject("employee_id") != null ? employeeDAO.getById(rs.getInt("employee_id")) : null;
                Storage storage = rs.getObject("storage_id") != null ? storageDAO.getById(rs.getInt("storage_id")) : null;

                HashMap<Product, Integer> orderMap = orderItemDAO.getByOrderId(orderId);

                orders.add(new Order(orderId, orderMap, storage, employee, customer));
            }
        } catch (SQLException | IOException e) {
            System.out.println("Ошибка: " + e);
        }
        return orders;
    }

}