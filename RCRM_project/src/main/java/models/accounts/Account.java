package models.accounts;

/**
 * Базовый абстрактный класс для всех типов аккаунтов.
 * Содержит общие атрибуты и методы для работы с учетными записями.
 */
public class Account {
    protected int id; // id аккаунта
    protected String username; // Имя пользователя
    protected String password; // Пароль пользователя

    /**
     * Конструктор со всеми атрибутами.
     * Используется для создания объекта с уже известным id (например, при загрузке из базы данных).
     *
     * id       - id аккаунта
     * username - Имя пользователя
     * password - Пароль пользователя
     */
    public Account(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Конструктор без id.
     * Используется для создания нового объекта, когда id еще неизвестен (например, перед сохранением в базу данных).
     *
     * username - Имя пользователя
     * password - Пароль пользователя
     */
    Account(String username, String password) {
        this(0, username, password); // Вызываем основной конструктор с id = -1
    }

    //Возвращает уникальный идентификатор аккаунта.
    public int getId() {
        return id;
    }

    //Возвращает Имя пользователя.
    public String getUsername() {
        return username;
    }

    //Устанавливает новый Имя пользователя.
    public void setUsername(String username) {
        this.username = username;
    }

    //Возвращает пароль пользователя
    public String getPassword() {
        return password;
    }

    // Устанавливает новый пароль пользователя.
    public void setPassword(String password) {
        this.password = password;
    }

    // Переопределенный метод toString для удобного вывода информации об аккаунте.
    @Override
    public String toString() {
        return "Account{ " +
                "login_id= " + id +
                ", username= '" + username + "' }";
    }
}