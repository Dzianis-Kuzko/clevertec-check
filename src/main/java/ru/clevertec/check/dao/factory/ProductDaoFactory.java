package ru.clevertec.check.dao.factory;

import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.dao.db.ProductJDBCDao;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.dao.db.ds.DatabaseConnectionFactory;
import ru.clevertec.check.dao.file.ProductFileDao;

public final class ProductDaoFactory {
    private static volatile IProductDao instance;

    private ProductDaoFactory() {
    }

    public static IProductDao getInstance(DatabaseConfig databaseConfig) {
        if (instance == null) {
            synchronized (ProductDaoFactory.class) {
                if (instance == null) {
                    instance = new ProductJDBCDao(databaseConfig);
                }
            }
        }
        return instance;
    }


}
