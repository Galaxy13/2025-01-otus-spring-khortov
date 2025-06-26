package com.galaxy13.hw.batch.reader;

import com.galaxy13.hw.model.mongo.MongoAuthor;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthorItemReader extends MongoPagingItemReader<MongoAuthor> {
    public AuthorItemReader(MongoTemplate mongoTemplate) {
        setTemplate(mongoTemplate);
        setTargetType(MongoAuthor.class);
        setQuery(new Query());
        setSort(Collections.singletonMap("id", Sort.Direction.ASC));
    }
}
