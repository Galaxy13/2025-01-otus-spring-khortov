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
VALUES (1, 3), (1, 2);

insert into genres_relationships (BOOK_ID, GENRE_ID)
values (2, 1);

insert into COMMENTS (COMMENT_TEXT, BOOK_ID)
values ( 'This book is really great', 1 ), ('Very nice book, enjoy to read this', 2),
       ('Post-Apocalypse mmm... my best', 2)