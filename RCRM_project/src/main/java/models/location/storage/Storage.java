package models.location.storage;

import models.BaseModel;
import models.location.City;


/**
 * Класс Storage представляет собой базовый класс для всех типов хранилищ.
 * Хранилища могут быть складами (Warehouse) или постаматами (Postamat).
 */
public class Storage extends BaseModel {
    protected String name; // Название хранилища
    protected City city; // Город, в котором находится хранилище
    protected Cell[] CellArr; // Массив ячеек хранилища
    protected int profit; // Прибыль от использования хранилища

    // ******************** КОнструкторы *****************
    /**
     * Конструктор для создания нового хранилища со всеми атрибутами.
     * Используется, когда нужно создать объект с уже известным ID.
     */
    public Storage(int id, String name, City city, int capacity, int profit) {
        super(id); // Устанавливаем ID
        this.name = name; // Устанавливаем название
        this.city = city; // Устанавливаем город
        this.CellArr = new Cell[capacity]; // Создаем массив ячеек заданной вместимости
        this.profit = profit; // Устанавливаем прибыль
    }

    /**
     * Конструктор для создания нового хранилища без указания прибыли.
     * Используется, когда прибыль изначально равна 0.
     */
    public Storage(int id, String name, City city, int capacity) {
        this(id, name, city, capacity, 0); // Вызываем основной конструктор с прибылью = 0
    }

    /**
     * Конструктор для создания нового хранилища без ID.
     * Используется, когда ID еще неизвестен (например, перед сохранением в базу данных).
     */
    public Storage(String name, City city, int capacity) {
        this(0, name, city, capacity, 0); // Вызываем основной конструктор с ID = 0
    }

    // ******************* Геттры и сеттеры **********************
    //геттер название хранилища
    public String getName() {
        return name;
    }

    //сеттер название хранилища
    public void setName(String name) {
        this.name = name;
    }

    //геттер города, в котором находится хранилище.
    public City getCity() {
        return city;
    }

    //сеттер массив ячеек хранилища
    public Cell[] getCellArr() {
        return CellArr;
    }

    // геттер вместимость хранилища (количество ячеек).
    public int getCapacity() {
        return CellArr.length;
    }

    // сеттер для массива склада
    public void setCellArr(Cell[] cellArr) {
        CellArr = cellArr;
    }

    // сеттер для ячейки массива склада
    public void setCellArr(int index, Cell cell) {
        CellArr[index]=cell;
    }

    // геттер дохода хранилища
    public int getProfit() {
        return profit;
    }

    // сеттер дохода хранилища
    public void setProfit(int profit) {
        this.profit = profit;
    }
}