package dao.storage;

import dao.DAO;
import dao.DBConnection;
import dao.other.CityDAO;
import models.location.City;
import models.location.storage.Storage;
import models.location.storage.psm.Postamat;
import models.location.storage.wrh.Warehouse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс StorageDAO предоставляет методы для работы с таблицей storages в базе данных.
 * Он используется для выполнения операций CRUD с хранилищами.
 */
public class StorageDAO implements DAO<Storage> {

    /**
     * Сохраняет данные о хранилище в базу данных.
     * Возвращает ID созданного хранилища.
     */
    @Override
    public int save(Storage storage) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO storages (name, city_id, storage_type, capacity) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, storage.getName()); // Название хранилища
            stmt.setInt(2, storage.getCity().getId()); // ID города
            stmt.setString(3, getStorageType(storage)); // Тип хранилища (склад или постамат)
            stmt.setInt(4, storage.getCellArr().length); // Вместимость

            stmt.executeUpdate();

            int generatedId;
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1); // Получаем сгенерированный ID
                } else {
                    throw new SQLException("Ошибка: не удалось получить ID хранилища.");
                }
            }
            return generatedId; // Возвращаем ID созданного хранилища
        } catch (SQLException e) {
            System.out.println(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1; // Возвращаем -1 в случае ошибки
    }

    /**
     * Обновляет данные существующего хранилища в базе данных.
     * Возвращает true, если обновление прошло успешно.
     */
    @Override
    public boolean update(Storage storage) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE storages SET name = ?, city_id = ?, capacity = ? WHERE id = ?")) {

            stmt.setString(1, storage.getName()); // Новое название
            stmt.setInt(2, storage.getCity().getId()); // Новый ID города
            stmt.setInt(3, storage.getCellArr().length); // Новая вместимость
            stmt.setInt(4, storage.getId()); // ID хранилища

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
     * Удаляет хранилище из базы данных по его ID.
     * Возвращает true, если удаление прошло успешно.
     */
    @Override
    public boolean delete(int id) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM storages WHERE id = ?")) {

            stmt.setInt(1, id); // ID хранилища

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Возвращаем true, если удаление прошло успешно
        } catch (SQLException | IOException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * Получает хранилище из базы данных по его ID.
     * Возвращает объект Storage или null, если хранилище не найдено.
     */
    @Override
    public Storage getById(int id) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM storages WHERE id = ?")) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStorage(rs); // Преобразуем ResultSet в объект Storage
                } else {
                    System.out.println("Хранилище с ID " + id + " не найдено.");
                    return null;
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Получает все хранилища из базы данных.
     * Возвращает список объектов Storage.
     */
    @Override
    public List<Storage> getAll() {
        List<Storage> storages = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM storages"); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                storages.add(mapResultSetToStorage(rs)); // Преобразуем ResultSet в объект Storage
            }
        } catch (SQLException | IOException e) {
            System.out.println(e);
        }

        return storages; // Возвращаем список хранилищ
    }

    /**
     * Преобразует ResultSet в объект Storage.
     * Определяет тип хранилища (склад или постамат) и создает соответствующий объект.
     */
    private Storage mapResultSetToStorage(ResultSet rs) throws SQLException {
        String storageType = rs.getString("storage_type"); // Тип хранилища
        int id = rs.getInt("id"); // ID хранилища
        String name = rs.getString("name"); // Название
        int cityId = rs.getInt("city_id"); // ID города
        int capacity = rs.getInt("capacity"); // Вместимость

        CityDAO cityDAO = new CityDAO();
        City city = cityDAO.getById(cityId); // Загружаем связанный объект City

        // Создаем объект в зависимости от типа хранилища
        switch (storageType) {
            case "WRH":
                return new Warehouse(id, name, city, capacity); // Склад
            case "PSM":
                return new Postamat(id, name, city, capacity); // Постамат
            default:
                throw new IllegalArgumentException("Неизвестный тип хранилища: " + storageType);
        }
    }

    /**
     * Определяет тип хранилища.
     * Возвращает строку "WRH" для склада или "PSM" для постамата.
     */
    private String getStorageType(Storage storage) {
        if (storage instanceof Warehouse) {
            return "WRH"; // Склад
        } else if (storage instanceof Postamat) {
            return "PSM"; // Постамат
        } else {
            throw new IllegalArgumentException("Неизвестный тип хранилища: " + storage.getClass().getName());
        }
    }
}