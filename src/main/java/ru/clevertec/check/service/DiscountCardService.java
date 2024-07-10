package ru.clevertec.check.service;

import ru.clevertec.check.core.dto.CreateDiscountCardDTO;
import ru.clevertec.check.core.dto.DiscountCardDTO;
import ru.clevertec.check.dao.api.IDiscountCardDao;
import ru.clevertec.check.service.api.IDiscountCardService;

public class DiscountCardService implements IDiscountCardService {
    private final IDiscountCardDao discountCardDao;

    public DiscountCardService(IDiscountCardDao discountCardDao) {
        this.discountCardDao = discountCardDao;
    }

    @Override
    public DiscountCardDTO get(long id) {
        return discountCardDao.get(id);
    }

    @Override
    public DiscountCardDTO create(CreateDiscountCardDTO item) {
        return discountCardDao.create(item);
    }

    @Override
    public void update(DiscountCardDTO item) {
        discountCardDao.update(item);
    }

    @Override
    public void delete(long id) {
        discountCardDao.delete(id);
    }

    @Override
    public DiscountCardDTO get(String number) {
        return discountCardDao.get(number);
    }
}
