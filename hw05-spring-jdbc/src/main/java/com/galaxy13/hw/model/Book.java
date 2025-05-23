package com.galaxy13.hw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private long id;

    private String title;

    private Author author;

    private List<Genre> genres;

    public void addGenre(Genre genre) {
        if (genres == null) {
            genres = new ArrayList<>();
        }
        genres.add(genre);
    }
}
