package ru.clevertec.check.service.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.dao.factory.ProductDaoFactory;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.service.api.IProductService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceFactoryTest {

    @Mock
    private DatabaseConfig databaseConfigMock;

    @Test
    void getInstance() {

        IProductService instance1 = ProductServiceFactory.getInstance(databaseConfigMock);
        IProductService instance2 = ProductServiceFactory.getInstance(databaseConfigMock);

        assertNotNull(instance1, "Первый вызов должен вернуть не-null экземпляр");
        assertNotNull(instance2, "Второй вызов должен вернуть не-null экземпляр");
        assertSame(instance1, instance2, "Должен возвращаться один и тот же экземпляр (синглтон)");
    }
}