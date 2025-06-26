package com.galaxy13.hw.batch.processor;

import com.galaxy13.hw.model.mongo.MongoAuthor;
import com.galaxy13.hw.model.jpa.JpaAuthor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class AuthorItemProcessor implements ItemProcessor<MongoAuthor, JpaAuthor> {
    @Override
    public JpaAuthor process(MongoAuthor author) {
        JpaAuthor jpaAuthor = new JpaAuthor();
        jpaAuthor.setFirstName(author.getFirstName());
        jpaAuthor.setLastName(author.getLastName());
        return jpaAuthor;
    }
}
