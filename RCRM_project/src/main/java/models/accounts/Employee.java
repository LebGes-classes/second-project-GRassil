package models.accounts;

import models.storage.Cell;
import models.storage.wrh.Warehouse;

public class Employee extends Account{
    private Warehouse warehouse;
    private POSITION position;

    public Employee(int id, String username, String password, Warehouse warehouse, POSITION position){
        super(id, username, password);
        this.warehouse = warehouse;
        this.position = position;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse whs) {
        warehouse = whs;
    }

    public POSITION getPosition() {
        return position;
    }

    public void setPosition(POSITION position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Employee{ " +
                super.toString() + " " +
                "warehouse= " + warehouse +
                ", position=" + position +
                '}';
    }
}
