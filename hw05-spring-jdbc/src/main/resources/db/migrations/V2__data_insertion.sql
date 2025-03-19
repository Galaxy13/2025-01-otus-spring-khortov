insert into genres (GENRE_TITLE)
values ('action'),
       ('comedy'),
       ('science fiction');

insert into authors (firstName, lastName)
values ('Lev', 'Tolstoy'),
       ('Henry', 'Thompson'),
       ('Stanislav', 'Lem');

insert into books (title, AUTHOR_ID)
values ('Fallout', 2),
       ('War and Peace', 1);

insert into genres_relationships (BOOK_ID, GENRE_ID)
VALUES (1, 3);

insert into genres_relationships (BOOK_ID, GENRE_ID)
values (2, 1);