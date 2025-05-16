package models.location.storage;

import models.BaseModel;

public class Cell<T> extends BaseModel {
    protected T obj;
    protected int quantity; // кол-во вещей
    protected Storage storage;

    public Cell(int id, T obj, int quantity, Storage storage) {
        super(id);
        this.obj = obj;
        this.quantity = quantity;
        this.storage = storage;
    }

    public T get() {
        return obj;
    }

    public void set(T obj) {
        this.obj = obj;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "id=" + getId() +
                ", obj=" + obj +
                ", quantity=" + quantity +
                ", storage=" + storage +
                '}';
    }
}
