package dao.other;

import dao.DAO;
import models.location.City;

import java.util.List;

public class CityDAO implements DAO<City> {
    @Override
    public int save(City obj) {
        return 0;
    }

    @Override
    public boolean update(City obj) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public City getById(int id) {
        return null;
    }

    @Override
    public List<City> getAll() {
        return List.of();
    }
}
