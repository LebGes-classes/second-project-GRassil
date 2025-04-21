package dao.storage;

import dao.DAO;
import dao.DBConnection;
import models.other.City;
import models.storage.Storage;
import models.storage.wrh.Warehouse;
import models.storage.SalesPoint;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StorageDAO implements DAO<Storage> {
    @Override
    public int save(Storage storage) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement( "INSERT INTO storages (name, city_id, storage_type, capacity) VALUES (?, ?, ?, ?)", 
                        Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, storage.getName());
            stmt.setInt(2, storage.getCity().getId());
            stmt.setString(3, getStorageType(storage)); // Определяем тип хранилища
            stmt.setInt(4, storage.getCellArr().length); // Вместимость

            stmt.executeUpdate();

            int generatedId;
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating storage failed, no ID obtained.");
                }
            }
            return generatedId;
        } catch (SQLException e) {
            System.out.println(e);;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Storage storage) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("UPDATE storages SET name = ?, city_id = ?, capacity = ? WHERE id = ?")) {

            stmt.setString(1, storage.getName());
            stmt.setInt(2, storage.getCity().getId());
            stmt.setInt(3, storage.getCellArr().length);
            stmt.setInt(4, storage.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Возвращаем true, если обновление прошло успешно
        } catch (SQLException e) {
            System.out.println(e);;
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM storages WHERE id = ?")) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Возвращаем true, если удаление прошло успешно
        } catch (SQLException | IOException e) {
            System.out.println(e);;
            return false;
        }
    }

    @Override
    public Storage getById(int id) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM storages WHERE id = ?")) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStorage(rs); // Преобразуем ResultSet в Storage
                } else {
                    System.out.println("Storage with ID " + id + " not found.");
                    return null;
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println(e);;
            return null;
        }
    }

    @Override
    public List<Storage> getAll() {
        List<Storage> storages = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM storages");
                    ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                storages.add(mapResultSetToStorage(rs)); // Преобразуем ResultSet в Storage
            }
        } catch (SQLException | IOException e) {
            System.out.println(e);;
        }

        return storages;
    }

    // Вспомогательный метод для преобразования ResultSet в Storage
    private Storage mapResultSetToStorage(ResultSet rs) throws SQLException {
        String storageType = rs.getString("storage_type");
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int cityId = rs.getInt("city_id");
        int capacity = rs.getInt("capacity");

        City city = CityDAO.getById(cityId); // Загружаем связанный объект City

        // Создаем объект в зависимости от типа хранилища
        if ("WRH".equalsIgnoreCase(storageType)) {
            return new Warehouse(id, name, city, capacity, WarehouseDAO.getById(id));
        } else if ("SLP".equalsIgnoreCase(storageType)) {
            return new SalesPoint(id, name, city, capacity, SalesPointDAO.getById(id));
        } else {
            throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
    }

    // Вспомогательный метод для определения типа хранилища
    private String getStorageType(Storage storage) {
        if (storage instanceof Warehouse) {
            return "WAREHOUSE";
        } else if (storage instanceof ShopStorage) {
            return "SHOP";
        } else {
            throw new IllegalArgumentException("Unknown storage type: " + storage.getClass().getName());
        }
    }

}