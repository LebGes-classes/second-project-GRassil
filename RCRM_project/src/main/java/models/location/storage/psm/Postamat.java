    package models.location.storage.psm;

    import models.location.City;
    import models.location.storage.Storage;



    public class Postamat extends Storage {
        public Postamat(int id, String name, City city, int capacity, int profit) {
            super(id, name, city, capacity, profit);
        }
        public Postamat(int id, String name, City city, int capacity) {
            super(id, name, city, capacity);
        }

        public Postamat(String name, City city, int capacity) {
            super(name, city, capacity);
        }

    }
