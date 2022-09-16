package com.edu.ulab.app.web;

import com.edu.ulab.app.facade.BookDataFacade;
import com.edu.ulab.app.web.constant.WebConstant;
import com.edu.ulab.app.web.request.BookRequest;
import com.edu.ulab.app.web.response.BookResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = WebConstant.VERSION_URL + "/book",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Book Controller",
        description = "CRUD operation with Book")
public class BookController {

    private final BookDataFacade bookDataFacade;

    @GetMapping("/{bookId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get book",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema =
                                    @Schema(implementation = BookResponse.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404",
                    description = "Book was not found",
                    content = @Content)

    })
    public BookResponse getBookById(@PathVariable Long bookId) {
        BookResponse bookResponse = bookDataFacade.getBook(bookId);
        log.info("Response with geting book -  {}", bookResponse);
        return bookResponse;
    }

    @DeleteMapping(value = "/{bookId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book was deleted",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema =
                                    @Schema(implementation = BookResponse.class)
                            )
                    }
            )
    })
    public void deleteBook(@PathVariable Long bookId) {
        log.info("Delete book with id {}", bookId);
        bookDataFacade.deleteBook(bookId);
    }

    @PutMapping(value = "/{bookId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update book",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema =
                                    @Schema(implementation = BookResponse.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404",
                    description = "Book id was not found",
                    content = @Content)

    })
    public BookResponse updateBook(@RequestBody BookRequest request, @PathVariable Long bookId) {
        BookResponse response = bookDataFacade.updateBook(request, bookId);
        log.info("Response with updated book: {}", response);
        return response;
    }
}
