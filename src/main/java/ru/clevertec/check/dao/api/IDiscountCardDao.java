package ru.clevertec.check.dao.api;

import ru.clevertec.check.core.dto.DiscountCardDTO;

public interface IDiscountCardDao{
    DiscountCardDTO get(String number);
}
