package ru.clevertec.check.controller.web.servlet.util;

import ru.clevertec.check.dao.db.ds.DatabaseConfig;

public class DatabaseConfigUtil {
    private final static String PARAM_DATASOURCE_URL = "datasource.url";
    private final static String PARAM_DATASOURCE_USERNAME = "datasource.username";
    private final static String PARAM_DATASOURCE_PASSWORD = "datasource.password";

    public static DatabaseConfig initializeDatabaseConfig() {
        String url = System.getProperty(PARAM_DATASOURCE_URL);
        String username = System.getProperty(PARAM_DATASOURCE_USERNAME);
        String password = System.getProperty(PARAM_DATASOURCE_PASSWORD);
        DatabaseConfig databaseConfig = new DatabaseConfig(url, username, password);

        return databaseConfig;
    }
}
