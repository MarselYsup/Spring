package com.edu.ulab.app.storage.base;

import java.util.List;
import java.util.Optional;

public interface Repository<K,T>{

    Optional<T> getById(K id);

    List<T> getAll();

    T save(T item);

    T update(K key, T item);

    void delete(K key);

}
