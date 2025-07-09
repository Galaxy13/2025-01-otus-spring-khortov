package com.galaxy13.hw;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;

@SpringBootTest
@SpringBatchTest
public abstract class AbstractBaseBatchTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @BeforeEach
    public void setUp() throws IOException {
        jobRepositoryTestUtils.removeJobExecutions();

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
