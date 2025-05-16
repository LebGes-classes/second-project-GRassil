package models.location;

import models.BaseModel;

public class City extends BaseModel {
    private final String name;
    City(int id, String name){
        super(id);
        this.name=name;
    }

    City(String name){
        this(0,name);
    }

    public String getName() {
        return name;
    }
}