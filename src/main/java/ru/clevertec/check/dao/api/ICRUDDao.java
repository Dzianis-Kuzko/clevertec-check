package main.java.ru.clevertec.check.dao.api;

public interface ICRUDDao<T> {
    T get(long id);
}
