package dao;

import java.util.List;

public interface DAO<T> {
    int save(T obj); // добавление записи
    boolean update(T obj); // изменение записи
    boolean delete(int id); // удаление записи
    T getById(int id); // получение записи по id
    List<T> getAll(); // Получить все записи таблицы
}