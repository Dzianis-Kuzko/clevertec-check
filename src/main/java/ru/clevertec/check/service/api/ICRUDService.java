package ru.clevertec.check.service.api;

public interface ICRUDService<T, S> {
    T get(long id);
    T create(S item);
    void update(T item);
    void delete(long id);
}
