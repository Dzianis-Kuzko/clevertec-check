package main.java.ru.clevertec.check.service.api;

import main.java.ru.clevertec.check.core.dto.DiscountCardDTO;

public interface IDiscountCardService {
    DiscountCardDTO get(String number);
}
