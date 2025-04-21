package models.order;

import models.accounts.Employee;
import models.storage.Storage;

import java.util.HashMap;

public class Order implements Comparable<Order>{
    private int id;
    private HashMap<Product, Integer> orderMap;
    private Storage storage;
    private Employee emp;

    Order(HashMap<Product, Integer> orderMap) {
        this(-1,orderMap, null,null);
    }

    Order(int id,HashMap<Product, Integer> orderMap, Storage storage, Employee emp){
        this.id = id;
        this.orderMap = orderMap;
        this.emp = emp;
        this.storage = storage;
    }



    // геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Product, Integer> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(HashMap<Product, Integer> orderMap) {
        this.orderMap = orderMap;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Employee getEmp() {
        return emp;
    }

    public void setEmp(Employee emp) {
        this.emp = emp;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderMap=" + orderMap +
                ", storage=" + storage +
                ", emp=" + emp +
                '}';
    }

    @Override
    public int compareTo(Order o) {
        int thisSize = orderMap.size();
        int oSize = o.getOrderMap().size();
        if (thisSize > oSize) return 1;
        else if (thisSize < oSize) { return -1; }
        return 0;
    }
}
