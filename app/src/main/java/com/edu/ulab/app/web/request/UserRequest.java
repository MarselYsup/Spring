package com.edu.ulab.app.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRequest {

    @Schema(description = "Full name of User", example = "Ivanov Ivan Ivanovich")
    private String fullName;

    @Schema(description = "Title of User", example = "Reader")
    private String title;

    @Schema(description = "Age of User", example = "22")
    private int age;
}
