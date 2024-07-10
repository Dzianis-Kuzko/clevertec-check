package ru.clevertec.check.dao.api;

import ru.clevertec.check.core.dto.CreateDiscountCardDTO;
import ru.clevertec.check.core.dto.DiscountCardDTO;

public interface IDiscountCardDao extends ICRUDDao<DiscountCardDTO, CreateDiscountCardDTO>{
    DiscountCardDTO get(String number);
}
