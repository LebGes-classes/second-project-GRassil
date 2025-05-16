package services.account;

import dao.acc.VendorDAO;
import models.account.Vendor;

public class VendorService {
    public static void save(Vendor acc) {
        VendorDAO vendorDAO = new VendorDAO();
        vendorDAO.save(acc);
    }
}
