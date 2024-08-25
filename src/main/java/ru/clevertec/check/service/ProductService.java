package ru.clevertec.check.service;

import ru.clevertec.check.core.dto.CreateProductDTO;
import ru.clevertec.check.core.dto.ProductDTO;
import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.service.api.IProductService;

public class ProductService implements IProductService {
    private final IProductDao productDao;

    public ProductService(IProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public ProductDTO get(long id) {
        return productDao.get(id);
    }

    @Override
    public ProductDTO create(CreateProductDTO item) {
        return this.productDao.create(item);
    }

    @Override
    public void update(ProductDTO item) {
        this.productDao.update(item);
    }

    @Override
    public void delete(long id) {
        this.productDao.delete(id);
    }
}
