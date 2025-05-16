package models.order;

import models.BaseModel;
import models.account.Customer;
import models.account.Employee;
import models.location.storage.Storage;

import java.util.HashMap;

// Класс Order - заказ, сделанный клиентом
// содержит информацию о продуктах в заказе, клиенте, складе и сотруднике, который обработал заказ.
public class Order extends BaseModel implements Comparable<Order> {
    private Customer customer; // Клиент, который сделал заказ
    private HashMap<Product, Integer> orderMap; // Карта продуктов и их количества в заказе
    private Storage storage; // Склад, с которого был выполнен заказ
    private Employee emp; // Сотрудник, который обработал заказ



    // ***************** КОнструкторы ************************

    /* Конструктор для создания заказа со всеми атрибутами. Используется, когда заказ уже сохранен в базе данных или нужно создать полный объект.
     *
     *  id       - уникальный идентификатор заказа
     *  orderMap - карта продуктов и их количества
     *  storage  - склад, с которого был выполнен заказ
     *  emp      - сотрудник, который обработал заказ
     *  customer - клиент, который сделал заказ
     */
    public Order(int id, HashMap<Product, Integer> orderMap, Storage storage, Employee emp, Customer customer) {
        super(id);
        this.orderMap = orderMap;
        this.emp = emp;
        this.storage = storage;
        this.customer = customer;
    }

    /* Конструктор для создания заказа со всеми атрибутами, кроме id. Используется, когда заказ еще не сохранен в базе данных.
     *
     *  orderMap - карта продуктов и их количества
     *  storage  - склад, с которого был выполнен заказ
     *  emp      - сотрудник, который обработал заказ
     *  customer - клиент, который сделал заказ
     */
    public Order(HashMap<Product, Integer> orderMap, Storage storage, Employee emp, Customer customer) {
        this(0, orderMap, storage,emp,customer);
    }

    /* Конструктор для создания нового заказа без ID. Используется, когда заказ еще не сохранен в базе данных.
     * orderMap - карта продуктов и их количества
     * customer - клиент, который сделал заказ
     */
    public Order(HashMap<Product, Integer> orderMap, Customer customer) {
        this(0, orderMap, null, null, customer); // Вызываем основной конструктор с ID = 0
    }

    // ******************* Геттеры и сеттеры ***********************

    //Возвращает клиента, который сделал заказ.
    public Customer getCustomer() {
        return customer;
    }

    //Возвращает словарь продуктов и их количества в заказе.
    public HashMap<Product, Integer> getOrderMap() {
        return orderMap;
    }

    //Устанавливает новый словарь продуктов и их количества.
    public void setOrderMap(HashMap<Product, Integer> orderMap) {
        this.orderMap = orderMap;
    }

    //Возвращает склад, с которого был выполнен заказ.
    public Storage getStorage() {
        return storage;
    }

    //Устанавливает новый склад для заказа.
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    //Возвращает сотрудника, который обработал заказ.
    public Employee getEmp() {
        return emp;
    }

    //Устанавливает нового сотрудника для заказа.
    public void setEmp(Employee emp) {
        this.emp = emp;
    }

    //Переопределенный метод toString для удобного вывода информации о заказе.
    @Override
    public String toString() {
        return "Order{" +
                "id="+ getId() +
                ", Customer=" + customer +
                ", orderMap=" + orderMap +
                ", storage=" + storage +
                ", emp=" + emp +
                '}';
    }

    /* Переопределенный метод compareTo для сравнения заказов по количеству продуктов.
     * Если текущий заказ больше, возвращается 1, если меньше - -1, если равны - 0.
     *
     * Параметр o - другой заказ для сравнения
     */
    @Override
    public int compareTo(Order o) {
        int thisSize = orderMap.size(); // Размер текущего заказа
        int oSize = o.getOrderMap().size(); // Размер другого заказа
        if (thisSize > oSize) return 1;
        else if (thisSize < oSize) return -1;
        return 0;
    }
}