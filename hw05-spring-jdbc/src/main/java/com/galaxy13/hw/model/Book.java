package com.galaxy13.hw.model;

import lombok.Data;

import java.util.List;

@Data
public class Book {
    private final long id;

    private final String title;

    private final Author author;

    private final List<Genre> genres;
}
