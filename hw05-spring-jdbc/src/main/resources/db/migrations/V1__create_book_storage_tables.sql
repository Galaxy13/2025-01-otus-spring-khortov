drop table if exists genres_relationships, authorRelationships, books, authors, genres;

create table authors
(
    id        bigserial primary key,
    firstName varchar(255) not null,
    lastName  varchar(255)
);

create table genres
(
    id         bigserial primary key,
    genreTitle varchar(255) not null unique
);

create table books
(
    id       bigserial primary key,
    title    varchar(255) not null unique,
    authorId bigint references authors (id) on delete cascade
);

create table genres_relationships
(
    bookId  bigint references books (id) on delete cascade,
    genreId bigint references genres (id) on delete cascade,
    primary key (bookId, genreId)
);

alter table authors
    add constraint authorCreds unique (firstName, lastName);

alter table books
    add constraint bookAuthor unique (title, authorId);