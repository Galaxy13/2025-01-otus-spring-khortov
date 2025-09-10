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
VALUES (1, 3),
       (1, 2);

insert into genres_relationships (BOOK_ID, GENRE_ID)
values (2, 1);

insert into CREDENTIALS (ID, USERNAME, PASSWORD, ROLE)
VALUES ( 'f5913be4-c456-423c-a4cb-0e89ca4f8afc', 'user',
        '$2a$12$XRog2Pex9JZQgrYnoWzbAexDtWEpKvU6p.I3wCjgP0jK.Dqc29D1i', 'USER' ),
       ('e477187a-a126-4b7c-8a7f-f52dec479f6a', 'galaxy',
        '$2a$12$mdsO.2ZGxx1HQfgvslF6iulzgKMFcu9v/w7388KzpyAVxXRx415Wm', 'USER'),
        ( '9137a02a-7b4a-4ea9-b741-8cd2752e64cd', 'super_admin',
         '$2a$12$qcYKSXcBKkGMw5XXm0P9d.qvr9wizkJw/iQo8eLvUY/mgf99Admay', 'ADMIN');

insert into COMMENTS (COMMENT_TEXT, BOOK_ID, USER_ID)
values ('This book is really great', 1, 'f5913be4-c456-423c-a4cb-0e89ca4f8afc'),
       ('Very nice book, enjoy to read this', 2, 'f5913be4-c456-423c-a4cb-0e89ca4f8afc'),
       ('Post-Apocalypse mmm... my best', 2, 'e477187a-a126-4b7c-8a7f-f52dec479f6a')