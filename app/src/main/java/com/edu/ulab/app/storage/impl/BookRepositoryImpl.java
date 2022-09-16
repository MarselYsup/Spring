package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.storage.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@Slf4j
public class BookRepositoryImpl implements BookRepository {

    //Key - BookId, Value - Book
    private final static Map<Long, BookEntity> storage = new HashMap<>();

    //Key - UserId, Value - Set of Books
    private final static Map<Long, Set<BookEntity>> oneToMany = new HashMap<>();

    private static Long sequenceId = 1L;

    @Override
    public Optional<BookEntity> getById(Long id) {

        if(Objects.isNull(id)) {
            log.error("Book id is null");
            throw new IllegalArgumentException(String.format("[%s] : Id object is null",
                    BookRepositoryImpl.class));
        }

        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<BookEntity> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public BookEntity save(BookEntity item) {
        if(Objects.isNull(item)) {
            log.error("Book for saving is null");
            throw new IllegalArgumentException(String.format("[%s] : Object for saving is null",
                    BookRepositoryImpl.class));
        }

        item.setId(sequenceId);
        storage.put(sequenceId, item);

        if(Objects.isNull(oneToMany.get(item.getUserId()))) {
            oneToMany.put(item.getUserId(), new HashSet<>());
        }

        oneToMany.get(item.getUserId()).add(item);
        sequenceId++;

        return item;
    }

    @Override
    public BookEntity update(Long key, BookEntity item) {
        if(Objects.isNull(key)) {
            log.error("Key for updating book is null");
            throw new IllegalArgumentException(String.format("[%s] : Key for updating object is null",
                    BookRepositoryImpl.class));
        }

        if(Objects.isNull(item)) {
            log.error("Book for updating is null");
            throw new IllegalArgumentException(String.format("[%s] : Object for updating is null",
                    BookRepositoryImpl.class));
        }

        return Optional.ofNullable(storage.get(key))
                .stream()
                .peek(book -> book.setAuthor(item.getAuthor()))
                .peek(book -> book.setPageCount(item.getPageCount()))
                .peek(book -> book.setTitle(item.getTitle()))
                .peek(book -> book.setUserId(item.getUserId()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("[%s] : Object with such key doesn't exist",
                        BookRepositoryImpl.class) ));


    }

    @Override
    public void delete(Long key) {

        if(Objects.isNull(key)) {
            log.error("Key for deleting book is null");
            throw new IllegalArgumentException(String.format("[%s] : Key for deleting object is null",
                    BookRepositoryImpl.class));
        }


        Optional.ofNullable(storage.remove(key))
                .ifPresentOrElse(book -> oneToMany.get(book.getUserId()).remove(book),
                        () -> log.warn("Book with such key - {} does not exist", key));


    }

    @Override
    public List<BookEntity> getBooksByUserId(Long userId) {
        if(Objects.isNull(userId)) {
            log.error("User id is null");
            throw new IllegalArgumentException(String.format("[%s] : User id is null",
                    BookRepositoryImpl.class));
        }

        return new ArrayList<>(oneToMany.get(userId));
    }
}
