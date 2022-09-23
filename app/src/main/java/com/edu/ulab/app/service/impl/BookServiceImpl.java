package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.exception.BadRequestException;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(BookDto bookDto) {
        if(Objects.isNull(bookDto)) {
            throw new BadRequestException(String.format("[%s] : BookDto is null",BookServiceImpl.class));
        }

        return bookMapper.bookEntityToBookDto(
                bookRepository.save(bookMapper.bookDtoToBookEntity(bookDto))
        );
    }

    @Override
    public BookDto updateBook(BookDto bookDto, Long id) {
        if(Objects.isNull(bookDto) || Objects.isNull(id)) {
            throw new BadRequestException(String.format("[%s] : BookDto or Id are null",BookServiceImpl.class));
        }
        return bookMapper.bookEntityToBookDto(
                bookRepository.update(id, bookMapper.bookDtoToBookEntity(bookDto))
        );
    }

    @Override
    public BookDto getBookById(Long id) {
        if(Objects.isNull(id)) {
            throw new BadRequestException(String.format("[%s] : Id is null",BookServiceImpl.class));
        }

        return bookMapper.bookEntityToBookDto(bookRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Book was not found with id - %d",id)))
        );
    }

    @Override
    public void deleteBookById(Long id) {
        if(Objects.isNull(id)) {
            throw new BadRequestException(String.format("[%s] : Id is null",BookServiceImpl.class));
        }

        bookRepository.delete(id);
    }

    @Override
    public List<BookDto> getBooksByUserId(Long userId) {
        if(Objects.isNull(userId)) {
            throw new BadRequestException(String.format("[%s] : BookDto is null",BookServiceImpl.class));
        }
        return bookRepository.getBooksByUserId(userId).stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookEntityToBookDto)
                .collect(Collectors.toList());
    }
}
