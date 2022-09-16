package com.edu.ulab.app.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {
    private Long bookId;

    private Long userId;

    @Schema(description = "Title of book", example = "Dead souls")
    private String title;

    @Schema(description = "First name and Last name of author", example = "Nikolai Gogol")
    private String author;

    @Schema(description = "The number of pages", example = "432")
    private Long pageCount;
}
