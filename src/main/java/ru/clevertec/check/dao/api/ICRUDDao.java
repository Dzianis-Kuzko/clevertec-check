package ru.clevertec.check.dao.api;

public interface ICRUDDao<T, S> {
    T get(long id);
    T create(S item);
    void update(T item);
    void delete(long id);
}
