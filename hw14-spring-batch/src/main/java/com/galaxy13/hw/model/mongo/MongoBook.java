package com.galaxy13.hw.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MongoBook {

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("author")
    private MongoAuthor author;

    @Field("genres")
    private List<MongoGenre> genres;

    public MongoBook(String title, MongoAuthor author, List<MongoGenre> genres) {
        this.title = title;
        this.author = author;
        this.genres = genres;
    }
}
