package com.galaxy13.hw.migrations.v1;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.validation.Validator;

@ChangeLog(
        order = "001"
)
public class InitialMigration {
    @ChangeSet(order = "000", id = "dropDB", runAlways = true, author = "galaxy13")
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initDb", runAlways = true, author = "galaxy13")
    public void execution(MongockTemplate mongoTemplate) {
        createAuthors(mongoTemplate);
        createComments(mongoTemplate);
        createBooks(mongoTemplate);
    }

    private void createComments(MongockTemplate mongoTemplate) {
        createCollectionWithValidation(mongoTemplate, "comments", """
            {
                $jsonSchema: {
                    bsonType: "object",
                    required: ["text", "book"],
                    properties: {
                        text: { bsonType: "string" },
                        book: {
                            bsonType: "object",
                            description: "Reference ID to Book document"
                        }
                    }
                }
            }
            """);
    }

    private void createBooks(MongockTemplate mongoTemplate) {
        createCollectionWithValidation(mongoTemplate, "books", """
            {
                $jsonSchema: {
                    bsonType: "object",
                    required: ["title", "author", "genres"],
                    properties: {
                        title: { bsonType: "string" },
                        author: {
                            bsonType: "object",
                        },
                        genres: {
                            bsonType: "array",
                            items: { bsonType: "object" }
                        },
                    }
                }
            }
            """);
    }

    private void createAuthors(MongockTemplate mongoTemplate) {
        createCollectionWithValidation(mongoTemplate, "authors", """
            {
                $jsonSchema: {
                    bsonType: "object",
                    required: ["firstName", "lastName"],
                    properties: {
                        firstName: { bsonType: "string" },
                        lastName: { bsonType: "string" },
                    }
                }
            }
            """);
    }

    private void createCollectionWithValidation(MongockTemplate mongoTemplate, String collectionName,
                                                String jsonValidator) {
        if (!mongoTemplate.collectionExists(collectionName)) {
            Validator validator = Validator.document(Document.parse(jsonValidator));
            mongoTemplate.createCollection(collectionName,
                    CollectionOptions.empty().validator(validator));

        }
    }
}
