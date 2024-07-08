package ru.clevertec.check.service.api;

import ru.clevertec.check.core.dto.DiscountCardDTO;

public interface IDiscountCardService {
    DiscountCardDTO get(String number);
}
