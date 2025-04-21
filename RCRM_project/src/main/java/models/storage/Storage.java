package models.storage;

import models.other.City;
import models.storage.wrh.WrhCell;

import java.math.BigDecimal;

public class Storage {
    protected int id;
    protected String name;
    protected City city;
    protected WrhCell[] wrhCellArr;
    protected BigDecimal profit = new BigDecimal(0);

    public Storage(int id, String name, City city, int capacity) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.wrhCellArr = new WrhCell[capacity];
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public WrhCell[] getCellArr() {
        return wrhCellArr;
    }

    public int getCapacity() { return wrhCellArr.length;}


}
