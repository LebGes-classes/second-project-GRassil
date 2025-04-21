package models.order;

public enum Category {
    TOY("TOY"),
    CLOTH("CLOTH"),
    TECHNIC("TECHNIC"),
    BOOK("BOOK"),
    FURNITURE("FURNITURE"),
    OTHER("OTHER");
    String name;
    Category(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
