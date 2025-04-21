package dao.storage;

import dao.DAO;
import models.storage.wrh.Warehouse;

import java.util.List;

public class WarehouseDAO implements DAO<Warehouse> {

    @Override
    public int save(Warehouse obj) {
        return 0;
    }

    @Override
    public boolean update(Warehouse obj) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Warehouse getById(int id) {
        return null;
    }

    @Override
    public List<Warehouse> getAll() {
        return List.of();
    }
}
