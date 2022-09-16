package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.web.request.BookRequest;
import com.edu.ulab.app.web.response.BookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BookDataFacade {

    private final BookService bookService;

    private final BookMapper bookMapper;

    public BookResponse getBook(Long id) {
        BookDto bookDto = bookService.getBookById(id);
        log.info("Got book {}", bookDto);

        return BookResponse.builder()
                .bookId(bookDto.getId())
                .author(bookDto.getAuthor())
                .pageCount(bookDto.getPageCount())
                .title(bookDto.getTitle())
                .userId(bookDto.getUserId())
                .build();
    }

    public BookResponse updateBook(BookRequest bookRequest, Long id) {
        log.info("got book to update request {} ", bookRequest);
        BookDto bookDto = bookMapper.bookRequestToBookDto(bookRequest);
        log.info("mapped book {} ", bookDto);

        BookDto updatedBook = bookService.updateBook(bookDto, id);
        log.info("Updated book {}", updatedBook);

        return BookResponse.builder()
                .bookId(updatedBook.getId())
                .author(updatedBook.getAuthor())
                .pageCount(updatedBook.getPageCount())
                .title(updatedBook.getTitle())
                .userId(updatedBook.getUserId())
                .build();
    }

    public void deleteBook(Long id) {
        bookService.deleteBookById(id);
        log.info("Book with id  - {} was deleted", id);
    }



}
