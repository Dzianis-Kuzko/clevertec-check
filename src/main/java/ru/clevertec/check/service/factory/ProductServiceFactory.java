package main.java.ru.clevertec.check.service.factory;

import main.java.ru.clevertec.check.dao.factory.ProductDaoFactory;
import main.java.ru.clevertec.check.service.ProductService;
import main.java.ru.clevertec.check.service.api.IProductService;

public final class ProductServiceFactory {
    private static  volatile IProductService instance;
    private ProductServiceFactory(){}

    public static IProductService getInstance(String filePath){
        if(instance==null){
            synchronized (ProductServiceFactory.class){
                if (instance==null){
                    instance = new ProductService(ProductDaoFactory.getInstance(filePath));
                }
            }
        }
        return instance;
    }
}
