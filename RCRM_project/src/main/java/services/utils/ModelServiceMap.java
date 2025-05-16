package services.utils;

import models.account.Account;
import models.account.Customer;
import models.account.Employee;
import models.account.Vendor;
import models.location.City;
import models.location.storage.psm.Postamat;
import models.location.storage.psm.PsmCell;
import models.location.storage.wrh.Warehouse;
import models.location.storage.wrh.WrhCell;
import models.order.Order;
import models.order.Product;
import services.account.AccountService;
import services.account.CustomerService;
import services.account.EmployeeService;
import services.account.VendorService;
import services.location.CityService;
import services.location.storage.psm.PostamatService;
import services.location.storage.psm.PsmCellService;
import services.location.storage.wrh.WarehouseService;
import services.location.storage.wrh.WrhCellService;
import services.order.OrderService;
import services.order.ProductService;

import java.util.HashMap;
import java.util.Map;

public class ModelServiceMap {
    public static <T> Class<?> getServiceClass(Class<T> modelClass){
        Map<Class<?>, Class<?>> serviceMap = new HashMap<>();
        serviceMap.put(Account.class, AccountService.class);
        serviceMap.put(Customer.class, CustomerService.class);
        serviceMap.put(Employee.class, EmployeeService.class);
        serviceMap.put(Vendor.class, VendorService.class);

        serviceMap.put(Product.class, ProductService.class);
        serviceMap.put(Order.class, OrderService.class);

        serviceMap.put(City.class, CityService.class);

        serviceMap.put(Postamat.class, PostamatService.class);
        serviceMap.put(PsmCell.class, PsmCellService.class);
        serviceMap.put(Warehouse.class, WarehouseService.class);
        serviceMap.put(WrhCell.class, WrhCellService.class);
    }
}
