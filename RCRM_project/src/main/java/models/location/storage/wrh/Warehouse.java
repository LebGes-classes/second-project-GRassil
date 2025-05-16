package models.location.storage.wrh;

import models.location.City;
import models.location.storage.Storage;

public class Warehouse extends Storage {
    public Warehouse(int id, String name, City city, int capacity, int profit) {
        super(id, name, city, capacity, profit);
    }
    public Warehouse(int id, String name, City city, int capacity) {
        super(id, name, city, capacity);
    }

    public Warehouse(String name, City city, int capacity) {
        super(name, city, capacity);
    }
}
