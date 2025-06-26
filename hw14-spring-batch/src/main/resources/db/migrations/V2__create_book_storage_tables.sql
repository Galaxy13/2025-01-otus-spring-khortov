CREATE TABLE authors
(
    id        bigserial primary key,
    firstname varchar(255) not null,
    lastName  varchar(255)
);

CREATE TABLE genres
(
    id          bigserial primary key,
    genre_title varchar(255) not null unique
);

CREATE TABLE books
(
    id        bigserial primary key,
    title     varchar(255) not null unique,
    author_id bigint references authors (id) on delete cascade
);

CREATE TABLE genres_relationships
(
    book_id  bigint references books (id) on delete cascade,
    genre_id bigint references genres (id) on delete cascade,
    primary key (book_id, genre_id)
);

CREATE TABLE comments
(
    id           bigserial primary key,
    comment_text varchar(255) not null,
    book_id      bigint references books (id) on delete cascade
);

ALTER TABLE authors
    ADD CONSTRAINT authorCreds UNIQUE (firstname, lastName);

ALTER TABLE books
    ADD CONSTRAINT bookAuthor UNIQUE (title, author_id);