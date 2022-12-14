package com.edu.ulab.app.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BookRequest {

    @Schema(description = "Title of Book", example = "Dead souls")
    private String title;

    @Schema(description = "First name and Last name of author", example = "Nikolai Gogol")
    private String author;

    @Schema(description = "The number of pages", example = "432")
    private Long pageCount;
}
