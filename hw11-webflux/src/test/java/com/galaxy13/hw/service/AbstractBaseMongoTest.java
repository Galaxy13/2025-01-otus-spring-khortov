package com.galaxy13.hw.service;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import java.io.IOException;

@DataMongoTest
public abstract class AbstractBaseMongoTest {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void setUp() throws IOException {
        mongoTemplate.getDb().drop();

        var resource = new ClassPathResource("data.json");
        JSONObject jsonData = (JSONObject) JSONValue.parse(resource.getInputStream());

        JSONArray authors = (JSONArray) jsonData.get("authors");
        mongoTemplate.insert(authors, "authors");

        JSONArray genres = (JSONArray) jsonData.get("genres");
        mongoTemplate.insert(genres, "genres");

        JSONArray books = (JSONArray) jsonData.get("books");
        mongoTemplate.insert(books, "books");

        JSONArray comments = (JSONArray) jsonData.get("comments");
        mongoTemplate.insert(comments, "comments");
    }
}
