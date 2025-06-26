package com.galaxy13.hw.batch.reader;

import com.galaxy13.hw.model.mongo.MongoGenre;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GenreItemReader extends MongoPagingItemReader<MongoGenre> {
    public GenreItemReader(MongoTemplate mongoTemplate) {
        setTemplate(mongoTemplate);
        setTargetType(MongoGenre.class);
        setQuery(new Query());
        setSort(Collections.singletonMap("id", Sort.Direction.ASC));
    }
}
