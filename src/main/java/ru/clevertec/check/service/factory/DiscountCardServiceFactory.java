package main.java.ru.clevertec.check.service.factory;

import main.java.ru.clevertec.check.dao.factory.DiscountCardDaoFactory;
import main.java.ru.clevertec.check.service.DiscountCardService;
import main.java.ru.clevertec.check.service.api.IDiscountCardService;

public class DiscountCardServiceFactory {
    private static volatile IDiscountCardService instance;

    private DiscountCardServiceFactory() {
    }

    public static IDiscountCardService getInstance(String filePath) {
        if (instance == null) {
            synchronized (DiscountCardServiceFactory.class) {
                if (instance == null) {
                    instance = new DiscountCardService(DiscountCardDaoFactory.getInstance(filePath));
                }
            }
        }
        return instance;
    }
}
