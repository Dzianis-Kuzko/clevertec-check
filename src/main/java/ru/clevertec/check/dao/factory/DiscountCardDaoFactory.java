package main.java.ru.clevertec.check.dao.factory;

import main.java.ru.clevertec.check.dao.api.IDiscountCardDao;
import main.java.ru.clevertec.check.dao.file.DiscountCardFileDao;

public final class DiscountCardDaoFactory {
    private static volatile IDiscountCardDao instance;

    private DiscountCardDaoFactory(){

    }

    public static IDiscountCardDao getInstance(String filePath){
        if(instance ==null){
            synchronized (DiscountCardDaoFactory.class){
                if (instance ==null){
                    instance= new DiscountCardFileDao(filePath);
                }
            }
        }
        return instance;
    }
}
