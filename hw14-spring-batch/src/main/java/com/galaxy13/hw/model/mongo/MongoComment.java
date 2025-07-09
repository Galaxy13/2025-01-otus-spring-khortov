package com.galaxy13.hw.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MongoComment {
    @Id
    private String id;

    @Field("text")
    private String text;

    @DBRef(lazy = true)
    private MongoBook book;

    public MongoComment(String text, MongoBook book) {
        this.text = text;
        this.book = book;
    }
}
