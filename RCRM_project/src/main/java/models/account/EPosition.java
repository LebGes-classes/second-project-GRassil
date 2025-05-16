package models.account;

public enum EPosition {
    WRK("WORKER"),
    MNG("MANAGER");
    String name;
    EPosition(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "EPosition - " + name ;
    }
}
