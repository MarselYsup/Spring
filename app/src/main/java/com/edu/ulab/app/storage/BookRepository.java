package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.storage.base.Repository;

import java.util.List;

public interface BookRepository extends Repository<Long, BookEntity> {
    List<BookEntity> getBooksByUserId(Long userId);
}
