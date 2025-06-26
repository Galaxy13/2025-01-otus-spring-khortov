package com.galaxy13.hw.batch.processor;

import com.galaxy13.hw.model.mongo.MongoGenre;
import com.galaxy13.hw.model.jpa.JpaGenre;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class GenreItemProcessor implements ItemProcessor<MongoGenre, JpaGenre> {
    @Override
    public JpaGenre process(MongoGenre genre) {
        JpaGenre jpaGenre = new JpaGenre();
        jpaGenre.setName(genre.getName());
        return jpaGenre;
    }
}
