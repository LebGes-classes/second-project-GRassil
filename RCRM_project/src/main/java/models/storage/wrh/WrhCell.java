package models.storage.wrh;

import models.order.Product;
import models.storage.Cell;
import models.storage.Storage;

public class WrhCell extends Cell<Product> {

    public WrhCell(int id, Product product, int quantity, Storage storage) {
        super(id, product, quantity, storage);
    }

}

