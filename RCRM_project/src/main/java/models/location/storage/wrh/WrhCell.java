package models.location.storage.wrh;

import models.order.Product;
import models.location.storage.Cell;
import models.location.storage.Storage;

public class WrhCell extends Cell<Product> {

    public WrhCell(int id, Product product, int quantity, Storage storage) {
        super(id, product, quantity, storage);
    }

}

