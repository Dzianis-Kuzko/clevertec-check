package main.java.ru.clevertec.check.dao.factory;

import main.java.ru.clevertec.check.dao.api.IProductDao;
import main.java.ru.clevertec.check.dao.file.ProductFileDao;

public final class ProductDaoFactory {
    private static volatile IProductDao instance;

    private ProductDaoFactory() {
    }

    public static IProductDao getInstance(String filePath) {
        if (instance == null) {
            synchronized (ProductDaoFactory.class) {
                if (instance == null) {
                    instance = new ProductFileDao(filePath);
                }
            }
        }
        return instance;
    }


}
