package dao.storage;

import dao.DAO;
import models.location.storage.psm.Postamat;

import java.util.List;

public class PostamatDAO implements DAO<Postamat> {

    @Override
    public int save(Postamat obj) {
        return 0;
    }

    @Override
    public boolean update(Postamat obj) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Postamat getById(int id) {
        return null;
    }

    @Override
    public List<Postamat> getAll() {
        return List.of();
    }
}
