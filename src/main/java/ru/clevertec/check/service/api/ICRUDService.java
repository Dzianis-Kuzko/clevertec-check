package main.java.ru.clevertec.check.service.api;

import main.java.ru.clevertec.check.core.dto.DiscountCardDTO;

public interface ICRUDService<T> {
    T get(long id);
}
