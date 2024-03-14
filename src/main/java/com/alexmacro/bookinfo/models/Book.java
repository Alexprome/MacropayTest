package com.alexmacro.bookinfo.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.UUID;

//Annotations to allow spring manage boiler plate code
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    UUID id;
    @NotEmpty
    @Pattern(regexp = "[a-zA-Z0-9 [,]]+", message = "")
    String title;


    @NotEmpty
    @Pattern(regexp = "[a-zA-Z0-9 ]+", message = "")
    String author;

    @PositiveOrZero
    BigDecimal price;

    @PositiveOrZero
    Integer availability;

    @PositiveOrZero
    Integer num_reviews;

    @PositiveOrZero
    Integer stars;

    @NotEmpty
    @Pattern(regexp = "[a-zA-Z0-9 [.,\\n]]+", message = "")
    @Size(min = 100, max = 600)
    String description;
}
