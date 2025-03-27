package com.galaxy13.hw.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Author {
    private long id;

    private String firstName;

    private String lastName;
}
