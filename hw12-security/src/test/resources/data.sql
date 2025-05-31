insert into authors(FIRSTNAME, LASTNAME)
values ('Author_1', 'Surname_1'),
       ('Author_2', 'Surname_2'),
       ('Author_3', 'Surname_3');

insert into genres(GENRE_TITLE)
values ('Genre_1'),
       ('Genre_2'),
       ('Genre_3'),
       ('Genre_4'),
       ('Genre_5'),
       ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1),
       ('BookTitle_2', 2),
       ('BookTitle_3', 3);

insert into GENRES_RELATIONSHIPS(book_id, genre_id)
values (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (3, 5),
       (3, 6);

insert into CREDENTIALS (ID, USERNAME, PASSWORD, ROLE)
VALUES ( 'f5913be4-c456-423c-a4cb-0e89ca4f8afc', 'user',
         '$2a$12$XRog2Pex9JZQgrYnoWzbAexDtWEpKvU6p.I3wCjgP0jK.Dqc29D1i', 'USER' ),
       ( '9137a02a-7b4a-4ea9-b741-8cd2752e64cd', 'admin',
         '$2a$12$qcYKSXcBKkGMw5XXm0P9d.qvr9wizkJw/iQo8eLvUY/mgf99Admay', 'ADMIN');

insert into COMMENTS(COMMENT_TEXT, BOOK_ID, USER_ID)
values ('C_1', 1, 'f5913be4-c456-423c-a4cb-0e89ca4f8afc'),
       ('C_2', 1, 'f5913be4-c456-423c-a4cb-0e89ca4f8afc'),
       ('C_3', 2, '9137a02a-7b4a-4ea9-b741-8cd2752e64cd'),
       ('C_4', 2, 'f5913be4-c456-423c-a4cb-0e89ca4f8afc'),
       ('C_5', 3, '9137a02a-7b4a-4ea9-b741-8cd2752e64cd'),
       ('C_6', 3, '9137a02a-7b4a-4ea9-b741-8cd2752e64cd')
