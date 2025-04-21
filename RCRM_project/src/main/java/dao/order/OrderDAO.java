package dao.order;

import dao.DAO;
import models.order.Order;

import java.util.List;

public class OrderDAO implements DAO<Order> {

    @Override
    public int save(Order obj) {
        return 0;
    }

    @Override
    public boolean update(Order obj) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Order getById(int id) {
        return null;
    }

    @Override
    public List<Order> getAll() {
        return List.of();
    }
}
