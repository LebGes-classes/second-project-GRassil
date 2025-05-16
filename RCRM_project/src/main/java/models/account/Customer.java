package models.account;

import models.location.storage.psm.Postamat;

public class Customer extends Account {
    private Postamat postamat; // Ссылка на постамат, с которым связан клиент

    // Конструктор со всеми атрибутами
    public Customer(int id, String username, String password, Postamat postamat) {
        super(id, username, password, EAccType.CUSTOMER); // Вызываем конструктор родительского класса Account
        this.postamat = postamat; // Устанавливаем связь с постаматом
    }

    public Customer(Account account) {
        super(account);
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