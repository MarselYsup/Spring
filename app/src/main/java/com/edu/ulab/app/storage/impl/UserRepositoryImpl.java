package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.storage.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final static Map<Long, UserEntity> storage = new HashMap<>();

    private static Long sequenceId = 1L;


    @Override
    public Optional<UserEntity> getById(Long id) {
        if(Objects.isNull(id)) {
            log.error("User id is null");
            throw new IllegalArgumentException(String.format("[%s] : Id object is null",
                    UserRepositoryImpl.class));
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<UserEntity> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public UserEntity save(UserEntity item) {

        if(Objects.isNull(item)) {
            log.error("User for saving is null");
            throw new IllegalArgumentException(String.format("[%s] : Object for saving is null", UserRepositoryImpl.class));
        }

        item.setId(sequenceId);
        storage.put(sequenceId, item);
        sequenceId++;

        return item;
    }

    @Override
    public UserEntity update(Long key, UserEntity item) {

        if(Objects.isNull(key)) {
            log.error("Key for updating user is null");
            throw new IllegalArgumentException(String.format("[%s] : Key for updating object is null",
                    UserRepositoryImpl.class));
        }

        if(Objects.isNull(item)) {
            log.error("User for updating is null");
            throw new IllegalArgumentException(String.format("[%s] : Object for updating is null",
                    UserRepositoryImpl.class));
        }

        return Optional.ofNullable(storage.get(key))
                .stream()
                .peek(user -> user.setAge(item.getAge()))
                .peek(user -> user.setTitle(item.getTitle()))
                .peek(user -> user.setFullName(item.getFullName()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("[%s] : Object with such key doesn't exist",
                        UserRepositoryImpl.class) ));
    }

    @Override
    public void delete(Long key) {
        if(Objects.isNull(key)) {
            log.error("Key for deleting user is null");
            throw new IllegalArgumentException(String.format("[%s] : Key for deleting object is null",
                    UserRepositoryImpl.class));
        }

        Optional.ofNullable(storage.remove(key))
                .ifPresentOrElse(user -> log.info("User was deleted {}", user),
                        () -> log.warn("User with such key - {} does not exist", key));

    }
}
