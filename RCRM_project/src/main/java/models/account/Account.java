package models.account;

import models.BaseModel;

/**
 * Базовый абстрактный класс для всех типов аккаунтов.
 * Содержит общие атрибуты и методы для работы с учетными записями.
 */
public class Account extends BaseModel {
    protected String username; // Имя пользователя
    protected String password; // Пароль пользователя
    protected EAccType eAccType; // Тип аккаунта


    // *********** КОнструкторы ***********************

    /* Конструктор со всеми атрибутами.
     * Используется для создания объекта с уже известным id (например, при загрузке из базы данных)
     * Id - id аккаунта
     * username - Имя пользователя
     * password - Пароль пользователя
     */
    public Account(int id, String username, String password, EAccType eAccType) {
        super(id);
        this.username = username;
        this.password = password;
        this.eAccType = eAccType;
    }

    /* Конструктор без id.
     * Используется для создания нового объекта, когда id еще неизвестен (например, перед сохранением в базу данных).
     *
     * Username - Имя пользователя
     * password - Пароль пользователя
     */
    public Account(String username, String password) {
        this(0, username, password, null); // Вызываем основной конструктор с id = -1
    }

    /*
     * Конструктор через передачу объекта другого аккаунта
     */
    public Account(Account acc) {
        this(acc.getId(), acc.getUsername(), acc.getPassword(), acc.getEAccType());
    }

    // *************** Геттеры и сеттеры ******************

    //Возвращает Имя пользователя.
    public String getUsername() {
        return username;
    }

    //Устанавливает новое Имя пользователя
    public void setUsername(String username) {
        this.username = username;
    }


    //Возвращает пароль пользователя
    public String getPassword() {
        return password;
    }

    // Устанавливает новый пароль пользователя
    public void setPassword(String password) {
        this.password = password;
    }


    // Возврат типа аккаунта
    public EAccType getEAccType() {
        return eAccType;
    }

    // Установление типа аккаунта
    public void setEAccType(EAccType eAccType) {
        this.eAccType = eAccType;
    }

    // *********** Прочие методы ****************************************

    // Переопределенный метод toString для удобного вывода информации об аккаунте.
    @Override
    public String toString() {
        return "Account{" +
                "\nunic id = " + getId() +
                "\nusername = " + username +
                "\n}\n";
    }
}