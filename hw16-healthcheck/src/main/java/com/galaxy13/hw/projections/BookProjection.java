package com.galaxy13.hw.projections;

import com.galaxy13.hw.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = Book.class, name = "book")
public interface BookProjection {
    String getTitle();

    @Value("#{target.author.firstName}, #{target.author.lastName}")
    String getAuthor();

    String getGenres();
}
