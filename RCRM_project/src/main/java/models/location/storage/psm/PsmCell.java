package models.location.storage.psm;

import models.order.Order;
import models.location.storage.Cell;
import models.location.storage.Storage;

public class PsmCell extends Cell<Order> {
    private int password; // пароль от ячейки

    public PsmCell(int id, Order obj, int quantity, Storage storage, int password) {
        super(id, obj, quantity, storage);
        this.password = password;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PsmCell{" +
                this.toString() +
                "password=" + password +
                '}';
    }
}
