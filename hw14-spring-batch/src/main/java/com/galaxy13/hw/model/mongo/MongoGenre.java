package com.galaxy13.hw.model.mongo;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "genres")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MongoGenre {
    @Id
    private String id;

    @Field("name")
    private String name;

    public MongoGenre(String name) {
        this.name = name;
    }
}
