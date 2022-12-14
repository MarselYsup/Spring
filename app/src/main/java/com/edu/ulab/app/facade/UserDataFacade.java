package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataFacade {

    private final UserService userService;

    private final BookService bookService;

    private final UserMapper userMapper;

    private final BookMapper bookMapper;


    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);

        List<Long> bookIdList = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(createdUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .collect(Collectors.toList());
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .age(createdUser.getAge())
                .title(createdUser.getTitle())
                .fullName(createdUser.getFullName())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest, Long id) {
        log.info("Got user book update request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto updatedUser = userService.updateUser(userDto, id);
        log.info("Updated user: {}", updatedUser);

        //add new books for user
        userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(updatedUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .collect(Collectors.toList());

        //get all books for user = new books + old books
        List<Long> bookIdList = bookService.getBooksByUserId(updatedUser.getId())
                .stream()
                .map(BookDto::getId)
                .collect(Collectors.toList());

        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(updatedUser.getId())
                .fullName(updatedUser.getFullName())
                .title(updatedUser.getTitle())
                .age(updatedUser.getAge())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        UserDto userDto = userService.getUserById(userId);
        log.info("Got user dto {}", userDto);

        List<Long> bookIdList = bookService.getBooksByUserId(userId)
                .stream()
                .map(BookDto::getId)
                .collect(Collectors.toList());
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(userDto.getId())
                .fullName(userDto.getFullName())
                .title(userDto.getTitle())
                .age(userDto.getAge())
                .booksIdList(bookIdList)
                .build();
    }

    public void deleteUserWithBooks(Long userId) {
        //delete books whose has relationship with user
        bookService.getBooksByUserId(userId)
                .stream()
                .filter(Objects::nonNull)
                .peek(book -> log.info("Deleted book {}",book ))
                .map(BookDto::getId)
                .peek(bookService::deleteBookById);


        userService.deleteUserById(userId);
        log.info("User with id - {} was deleted", userId);


    }
}
