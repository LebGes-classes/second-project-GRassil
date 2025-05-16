package dao.storage;

import dao.DBConnection;
import dao.DAO;
import dao.other.CityDAO;
import models.location.City;
import models.location.storage.wrh.Warehouse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс WarehouseDAO предоставляет методы для работы с таблицей warehouses в базе данных.
 * Он используется для выполнения операций CRUD со складами.
 */
public class WarehouseDAO implements DAO<Warehouse> {
    StorageDAO storageDAO = new StorageDAO();


    /**
     * Сохраняет данные о складе в базу данных.
     * Возвращает ID созданного склада.
     */
    @Override
    public int save(Warehouse warehouse) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO warehouses (name, city_id, capacity, profit) VALUES (?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, warehouse.getName()); // Название склада
            stmt.setInt(2, warehouse.getCity().getId()); // ID города
            stmt.setInt(3, warehouse.getCellArr().length); // Вместимость
            stmt.setInt(4, warehouse.getProfit()); // Прибыль

            stmt.executeUpdate();

            int generatedId;
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1); // Получаем сгенерированный ID
                } else {
                    throw new SQLException("Ошибка: не удалось получить ID склада.");
                }
            }
            return generatedId; // Возвращаем ID созданного склада
        } catch (SQLException e) {
            System.out.println(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1; // Возвращаем -1 в случае ошибки
    }

    /**
     * Обновляет данные существующего склада в базе данных.
     * Возвращает true, если обновление прошло успешно.
     */
    @Override
    public boolean update(Warehouse warehouse) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE warehouses SET name = ?, city_id = ?, capacity = ?, profit = ? WHERE id = ?")) {

            stmt.setString(1, warehouse.getName()); // Новое название
            stmt.setInt(2, warehouse.getCity().getId()); // Новый ID города
            stmt.setInt(3, warehouse.getCellArr().length); // Новая вместимость
            stmt.setInt(4, warehouse.getProfit()); // Новая прибыль
            stmt.setInt(5, warehouse.getId()); // ID склада

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Возвращаем true, если обновление прошло успешно
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Удаляет склад из базы данных по его ID.
     * Возвращает true, если удаление прошло успешно.
     */
    @Override
    public boolean delete(int id) {
        return storageDAO.delete();
    }

    /**
     * Получает склад из базы данных по его ID.
     * Возвращает объект Warehouse или null, если склад не найден.
     */
    @Override
    public Warehouse getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM warehouses WHERE id = ?")) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToWarehouse(rs); // Преобразуем ResultSet в объект Warehouse
                } else {
                    System.out.println("Склад с ID " + id + " не найден.");
                    return null;
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Получает все склады из базы данных.
     * Возвращает список объектов Warehouse.
     */
    @Override
    public List<Warehouse> getAll() {
        List<Warehouse> warehouses = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM warehouses");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                warehouses.add(mapResultSetToWarehouse(rs)); // Преобразуем ResultSet в объект Warehouse
            }
        } catch (SQLException | IOException e) {
            System.out.println(e);
        }

        return warehouses; // Возвращаем список складов
    }

    /**
     * Преобразует ResultSet в объект Warehouse.
     */
    private Warehouse mapResultSetToWarehouse(ResultSet rs) throws SQLException {
        int id = rs.getInt("id"); // ID склада
        String name = rs.getString("name"); // Название
        int cityId = rs.getInt("city_id"); // ID города
        int capacity = rs.getInt("capacity"); // Вместимость
        int profit = rs.getInt("profit"); // Прибыль

        // Загружаем связанный объект City
        CityDAO cityDAO = new CityDAO();
        City city = cityDAO.getById(cityId);

        return new Warehouse(id, name, city, capacity, profit); // Создаем и возвращаем объект Warehouse
    }
}