package models.account;

import models.location.storage.wrh.Warehouse;

public class Employee extends Account{
    private Warehouse warehouse;
    private EPosition EPosition;

    public Employee(int id, String username, String password, Warehouse warehouse, EPosition EPosition){
        super(id,username, password, EAccType.EMPLOYEE);
        this.warehouse = warehouse;
        this.EPosition = EPosition;
    }

    public Employee(Account account){ super(account); }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse whs) {
        warehouse = whs;
    }

    public EPosition getPosition() {
        return EPosition;
    }

    public void setPosition(EPosition EPosition) {
        this.EPosition = EPosition;
    }

    @Override
    public String toString() {
        return "Employee:\n" +
                super.toString() + " " +
                "\nwarehouse= " + warehouse +
                "\nEPosition=" + EPosition +
                "\n...\n";
    }
}
