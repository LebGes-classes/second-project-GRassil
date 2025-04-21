package models.other;

public class City {
    private int id;
    private final String name;
    City(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}