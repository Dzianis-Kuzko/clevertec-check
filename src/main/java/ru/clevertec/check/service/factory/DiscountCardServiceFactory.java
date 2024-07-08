package ru.clevertec.check.service.factory;

import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.dao.factory.DiscountCardDaoFactory;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.service.api.IDiscountCardService;

public class DiscountCardServiceFactory {
    private static volatile IDiscountCardService instance;

    private DiscountCardServiceFactory() {
    }

    public static IDiscountCardService getInstance(DatabaseConfig databaseConfig) {
        if (instance == null) {
            synchronized (ProductServiceFactory.class) {
                if (instance == null) {
                    instance = new DiscountCardService(DiscountCardDaoFactory.getInstance(databaseConfig));
                }
            }
        }
        return instance;
    }
}
