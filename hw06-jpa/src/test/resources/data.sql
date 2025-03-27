insert into authors(FIRSTNAME, LASTNAME)
values ('Author_1', 'Surname_1'), ('Author_2', 'Surname_2'), ('Author_3', 'Surname_3');

insert into genres(GENRE_TITLE)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into GENRES_RELATIONSHIPS(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);