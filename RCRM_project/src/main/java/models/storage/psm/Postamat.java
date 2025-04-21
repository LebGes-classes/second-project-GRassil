package models.storage.psm;

import models.order.Order;
import models.other.City;
import models.storage.Storage;

import java.util.HashSet;

public class Postamat extends Storage {
    HashSet<Order> readyOrders;

    public Postamat(int id, String name, City city, int capacity, HashSet<Order> readyOrders) {
        super(id, name, city, wrhCellArr);
        this.readyOrders = readyOrders;
    }

    public HashSet<Order> getReadyOrders() {
        return readyOrders;
    }

    public void setReadyOrders(HashSet<Order> readyOrders) {
        this.readyOrders = readyOrders;
    }
}
