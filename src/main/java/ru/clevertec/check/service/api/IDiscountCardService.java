package ru.clevertec.check.service.api;

import ru.clevertec.check.core.dto.CreateDiscountCardDTO;
import ru.clevertec.check.core.dto.DiscountCardDTO;

public interface IDiscountCardService extends ICRUDService<DiscountCardDTO, CreateDiscountCardDTO>{
    DiscountCardDTO get(String number);
}
