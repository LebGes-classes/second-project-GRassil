package models.accounts;

public enum POSITION {
    WRK("WRK"),
    MNG("MNG");
    String name;
    POSITION(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "POSITION - " + name ;
    }
}
