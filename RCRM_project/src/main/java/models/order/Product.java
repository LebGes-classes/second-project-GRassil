package models.order;

import models.BaseModel;
import models.account.Vendor;
import models.location.storage.Storage;


public class Product extends BaseModel {
    private String name;
    private String description;
    int price;
    Category category;
    Storage storage;
    Vendor vendor;

    // ********** Конструкторы *************

    // Конструктор со всеми полями
    public Product(int id,String name, String description, int price, Category category, Storage storage, Vendor vendor) {
        super(id);
        this.name = name;
        this.description = description;

        if (price>=0){
            this.price = price;
        } else{
            throw new IllegalArgumentException("Цена должна быть больше 0");
        }

        this.category = category;
        this.storage = storage;
        this.vendor = vendor;
    }

    public Product(String name, String description, int price, Category category, Storage storage, Vendor vendor) {
        this(0,name, description, price, category, storage,vendor);
    }

    public Product(int id,String name, String description, int price) {
        this(id,name, description, price,null,null,null);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String decription) {
        this.description = decription;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        if (price>=0) this.price = price;
        else throw new IllegalArgumentException("Цена должна быть больше 0");
    }

    public String getCategory() {
        return category.toString();
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Vendor getVendor() {
        return vendor;
    }
    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    @Override
    public String toString() {
        return "Product{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category=" + category +
                '}';
    }

}
