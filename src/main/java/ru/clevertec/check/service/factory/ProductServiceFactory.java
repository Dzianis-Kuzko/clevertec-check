package ru.clevertec.check.service.factory;

import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.dao.factory.ProductDaoFactory;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.service.api.IProductService;

public final class ProductServiceFactory {
    private static volatile IProductService instance;

    private ProductServiceFactory() {
    }

    public static IProductService getInstance(DatabaseConfig databaseConfig) {
        if (instance == null) {
            synchronized (ProductServiceFactory.class) {
                if (instance == null) {
                    instance = new ProductService(ProductDaoFactory.getInstance(databaseConfig));
                }
            }
        }
        return instance;
    }
}
