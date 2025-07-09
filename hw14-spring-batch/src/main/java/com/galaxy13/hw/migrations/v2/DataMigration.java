package com.galaxy13.hw.migrations.v2;

import com.galaxy13.hw.model.mongo.MongoAuthor;
import com.galaxy13.hw.model.mongo.MongoBook;
import com.galaxy13.hw.model.mongo.MongoComment;
import com.galaxy13.hw.model.mongo.MongoGenre;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;

import java.util.List;

@ChangeLog(order = "002")
public class DataMigration {

    @ChangeSet(order = "000", id = "insertData", author = "galaxy13", runAlways = true)
    public void execute(MongockTemplate mongoTemplate) {
        MongoGenre action = new MongoGenre("action");
        MongoGenre comedy = new MongoGenre("comedy");
        MongoGenre sciFi = new MongoGenre("science fiction");
        mongoTemplate.insertAll(List.of(action, comedy, sciFi));

        MongoAuthor tolstoy = new MongoAuthor("Lev", "Tolstoy");
        MongoAuthor thompson = new MongoAuthor("Henry", "Thompson");
        MongoAuthor lem = new MongoAuthor("Stanislav", "Lem");
        mongoTemplate.insertAll(List.of(tolstoy, thompson, lem));

        MongoBook fallout = new MongoBook("Fallout", thompson, List.of(action, sciFi));
        MongoBook warAndPeace = new MongoBook("War and Peace", tolstoy, List.of(action, comedy));
        MongoBook solaris = new MongoBook("Solaris", lem, List.of(action));
        MongoBook annaKarenina = new MongoBook("Anna Karenina", tolstoy, List.of(comedy));
        mongoTemplate.insertAll(List.of(fallout, warAndPeace, solaris, annaKarenina));

        List<MongoComment> comments = List.of(
                new MongoComment("This book is really great", fallout),
                new MongoComment("Very nice book, enjoy to read this", warAndPeace),
                new MongoComment("Post-Apocalypse mmm... my best", warAndPeace)
        );
        mongoTemplate.insertAll(comments);
    }
}
