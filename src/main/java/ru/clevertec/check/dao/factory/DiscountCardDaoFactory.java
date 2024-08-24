package ru.clevertec.check.dao.factory;

import ru.clevertec.check.dao.api.IDiscountCardDao;
import ru.clevertec.check.dao.db.DiscountCardJDBCDao;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;

public final class DiscountCardDaoFactory {
    private static volatile IDiscountCardDao instance;

    private DiscountCardDaoFactory() {

    }

    public static IDiscountCardDao getInstance(DatabaseConfig databaseConfig) {
        if (instance == null) {
            synchronized (DiscountCardDaoFactory.class) {
                if (instance == null) {
                    instance = new DiscountCardJDBCDao(databaseConfig);
                }
            }
        }
        return instance;
    }
}
