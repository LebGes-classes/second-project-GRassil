package models.accounts;

import models.storage.psm.Postamat;

public class Customer extends Account {
    private Postamat postamat; // Ссылка на постамат, с которым связан клиент

    // Конструктор со всеми атрибутами
    public Customer(int id, String username, String password, Postamat postamat) {
        super(id, username, password); // Вызываем конструктор родительского класса Account
        this.postamat = postamat; // Устанавливаем связь с постаматом
    }

    // Конструктор без ID (для создания нового объекта)
    public Customer(String username, String password, Postamat postamat) {
        super(username, password); // Вызываем конструктор родительского класса Account
        this.postamat = postamat; // Устанавливаем связь с постаматом
    }

    // Геттер для получения постамата
    public Postamat getPostamat() {
        return postamat;
    }

    // Сеттер для установки постамата
    public void setPostamat(Postamat postamat) {
        this.postamat = postamat;
    }

    // Переопределенный метод toString для удобного вывода информации о клиенте
    @Override
    public String toString() {
        return "Customer{" +
                super.toString() + " " + // Выводим информацию об Аккаунте из родительского класса
                ", postamat=" + postamat + // Выводим информацию о постамате
                '}';
    }
}