package com.galaxy13.hw.migrations.v2;

import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Comment;
import com.galaxy13.hw.model.Genre;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@ChangeUnit(id = "data-migration", order = "002")
public class DataMigration {

    @Execution
    public void execute(MongoTemplate mongoTemplate) {
        Genre action = new Genre("action");
        Genre comedy = new Genre("comedy");
        Genre sciFi = new Genre("science fiction");
        mongoTemplate.insertAll(List.of(action, comedy, sciFi));

        Author tolstoy = new Author("Lev", "Tolstoy");
        Author thompson = new Author("Henry", "Thompson");
        Author lem = new Author("Stanislav", "Lem");
        mongoTemplate.insertAll(List.of(tolstoy, thompson, lem));

        Book fallout = new Book("Fallout", thompson, List.of(action, sciFi));
        Book warAndPeace = new Book("War and Peace", tolstoy, List.of(action, comedy));
        Book solaris = new Book("Solaris", lem, List.of(action));
        Book annaKarenina = new Book("Anna Karenina", tolstoy, List.of(comedy));
        mongoTemplate.insertAll(List.of(fallout, warAndPeace, solaris, annaKarenina));

        List<Comment> comments = List.of(
                new Comment("This book is really great", fallout),
                new Comment("Very nice book, enjoy to read this", warAndPeace),
                new Comment("Post-Apocalypse mmm... my best", warAndPeace)
        );
        mongoTemplate.insertAll(comments);
    }

    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        mongoTemplate.remove(new Query(), Genre.class);
        mongoTemplate.remove(new Query(), Author.class);
        mongoTemplate.remove(new Query(), Book.class);
        mongoTemplate.remove(new Query(), Comment.class);
    }
}
