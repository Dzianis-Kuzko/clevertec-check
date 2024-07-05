package main.java.ru.clevertec.check.service.api;

import main.java.ru.clevertec.check.core.dto.DiscountCardDTO;
import main.java.ru.clevertec.check.dao.api.ICRUDDao;

public interface IDiscountCardService  {
    DiscountCardDTO get(String number);
}
