package ru.clevertec.check.controller.web.servlet.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseConfigUtilTest {

    private static final String PARAM_DATASOURCE_URL = "datasource.url";
    private static final String PARAM_DATASOURCE_USERNAME = "datasource.username";
    private static final String PARAM_DATASOURCE_PASSWORD = "datasource.password";

    @BeforeEach
    void setUp() {
        System.setProperty(PARAM_DATASOURCE_URL, "url");
        System.setProperty(PARAM_DATASOURCE_USERNAME, "username");
        System.setProperty(PARAM_DATASOURCE_PASSWORD, "password");
    }

    @AfterEach
    void tearDown() {
        System.clearProperty(PARAM_DATASOURCE_URL);
        System.clearProperty(PARAM_DATASOURCE_USERNAME);
        System.clearProperty(PARAM_DATASOURCE_PASSWORD);
    }

    @Test
    void testInitializeDatabaseConfig() {
        DatabaseConfig config = DatabaseConfigUtil.initializeDatabaseConfig();

        assertEquals("url", config.getUrl());
        assertEquals("username", config.getUsername());
        assertEquals("password", config.getPassword());
    }
}