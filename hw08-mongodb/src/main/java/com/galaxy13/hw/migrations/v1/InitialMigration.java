package com.galaxy13.hw.migrations.v1;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.validation.Validator;

@ChangeUnit(
        id = "initial-setup",
        order = "001",
        runAlways = true
)
public class InitialMigration {

    @Execution
    public void execution(MongoTemplate mongoTemplate) {
        createAuthors(mongoTemplate);
        createComments(mongoTemplate);
        createBooks(mongoTemplate);
    }

    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        mongoTemplate.dropCollection("books");
        mongoTemplate.dropCollection("comments");
    }

    private void createComments(MongoTemplate mongoTemplate) {
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

    private void createBooks(MongoTemplate mongoTemplate) {
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

    private void createAuthors(MongoTemplate mongoTemplate) {
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

    private void createCollectionWithValidation(MongoTemplate mongoTemplate, String collectionName,
                                                String jsonValidator) {
        if (!mongoTemplate.collectionExists(collectionName)) {
            Validator validator = Validator.document(Document.parse(jsonValidator));
            mongoTemplate.createCollection(collectionName,
                    CollectionOptions.empty().validator(validator));

        }
    }
}
