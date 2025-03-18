drop table if exists genresRelationships, authorRelationships, books, authors, genres;

create table authors
(
    id        long primary key auto_increment not null unique,
    firstName varchar(50)                     not null,
    lastName  varchar(50)
);

create table genres
(
    id         long primary key auto_increment not null unique,
    genreTitle varchar(100)                    not null
);

create table books
(
    id       long primary key auto_increment not null unique,
    title    varchar(100)                    not null,
    authorId long                            not null references authors (id)
);

create table genresRelationships
(
    id      long primary key auto_increment not null unique,
    bookId  long                            not null references books (id),
    genreId long                            not null references genres (id)
);

insert into genres (genreTitle)
values ('action'),
       ('comedy'),
       ('science fiction');

insert into authors (firstName, lastName)
values ('Lev', 'Tolstoy'),
       ('Henry', 'Thompson'),
       ('Stanislav', 'Lem');

insert into books (title, authorId)
values ('War and Peace', 1);


insert into genresRelationships (bookId, genreId)
VALUES (1, 3);

insert into genresRelationships (bookId, genreId)
values (1, 2);

select (books.id, books.title, books.authorId, a.firstName, a.lastName, g2.id, g2.genreTitle)
from books
         left join genresRelationships gR on books.id = gR.bookId
         join genres g2 on gR.genreId = g2.id
         join authors a on books.authorId = a.id;



