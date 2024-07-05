package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.core.dto.DiscountCardDTO;
import main.java.ru.clevertec.check.dao.api.IDiscountCardDao;
import main.java.ru.clevertec.check.service.api.IDiscountCardService;

public class DiscountCardService implements IDiscountCardService {
    private final IDiscountCardDao discountCardDao;

    public DiscountCardService(IDiscountCardDao discountCardDao) {
        this.discountCardDao = discountCardDao;
    }

    @Override
    public DiscountCardDTO get(String number) {
        return discountCardDao.get(number);
    }
}
