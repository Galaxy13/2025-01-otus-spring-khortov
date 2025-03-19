insert into genres (genreTitle)
values ('action'),
       ('comedy'),
       ('science fiction');

insert into authors (firstName, lastName)
values ('Lev', 'Tolstoy'),
       ('Henry', 'Thompson'),
       ('Stanislav', 'Lem');

insert into books (title, authorId)
values ('Fallout', 2),
       ('War and Peace', 1);

insert into genres_relationships (bookId, genreId)
VALUES (1, 3);

insert into genres_relationships (bookId, genreId)
values (2, 1);