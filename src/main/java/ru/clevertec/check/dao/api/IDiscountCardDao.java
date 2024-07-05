package main.java.ru.clevertec.check.dao.api;

import main.java.ru.clevertec.check.core.dto.DiscountCardDTO;

public interface IDiscountCardDao{
    DiscountCardDTO get(String number);
}
