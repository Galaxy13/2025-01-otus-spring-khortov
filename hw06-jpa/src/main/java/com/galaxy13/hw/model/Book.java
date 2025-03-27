package com.galaxy13.hw.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Table(name = "books")
@NamedEntityGraph(name = "author-entity-graph", attributeNodes = {@NamedAttributeNode("author")})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {

    public Book (long id, String title, Author author, List<Genre> genres) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genres = genres;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = Author.class)
    @JoinColumn(name = "author_id")
    private Author author;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Genre.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "genres_relationships", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = Comment.class, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "book_id")
    private List<Comment> comments;
}
