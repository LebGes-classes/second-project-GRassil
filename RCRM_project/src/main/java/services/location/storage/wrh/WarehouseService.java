package services.location.storage.wrh;

import models.location.storage.wrh.Warehouse;
import dao.storage.WarehouseDAO;

import java.util.List;

public class WarehouseService {
    public static List<Warehouse> getAll(){
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        return warehouseDAO.getAll();
    }

    public static Warehouse get(int id){
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        return warehouseDAO.getById(id);
    }

}
