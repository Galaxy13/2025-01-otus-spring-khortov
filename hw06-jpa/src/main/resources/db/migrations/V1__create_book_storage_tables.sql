drop table if exists genres_relationships, authorRelationships, books, authors, genres;

create table authors
(
    id        bigserial primary key,
    firstname varchar(255) not null,
    lastName  varchar(255)
);

create table genres
(
    id         bigserial primary key,
    genre_title varchar(255) not null unique
);

create table books
(
    id       bigserial primary key,
    title    varchar(255) not null unique,
    author_id bigint references authors (id) on delete cascade
);

create table genres_relationships
(
    book_id  bigint references books (id) on delete cascade,
    genre_id bigint references genres (id) on delete cascade,
    primary key (book_id, genre_id)
);

create table comments
(
    id           bigserial primary key,
    comment_text varchar(255) not null,
    book_id      bigint references books (id)
);

alter table authors
    add constraint authorCreds unique (firstname, lastName);

alter table books
    add constraint bookAuthor unique (title, author_id);