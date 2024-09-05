package ru.clevertec.check.service.factory;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.service.api.IDiscountCardService;
import ru.clevertec.check.service.api.IProductService;

import static org.junit.jupiter.api.Assertions.*;

class DiscountCardServiceFactoryTest {

    @Mock
    private DatabaseConfig databaseConfigMock;

    @Test
    void getInstance() {

        IDiscountCardService instance1 = DiscountCardServiceFactory.getInstance(databaseConfigMock);
        IDiscountCardService instance2 = DiscountCardServiceFactory.getInstance(databaseConfigMock);

        assertNotNull(instance1, "Первый вызов должен вернуть не-null экземпляр");
        assertNotNull(instance2, "Второй вызов должен вернуть не-null экземпляр");
        assertSame(instance1, instance2, "Должен возвращаться один и тот же экземпляр (синглтон)");
    }

}