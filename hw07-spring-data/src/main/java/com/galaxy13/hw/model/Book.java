package com.galaxy13.hw.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Table(name = "books")
@NamedEntityGraph(name = "author-entity-graph", attributeNodes = {@NamedAttributeNode("author")})
@NamedEntityGraph(name = "author-genre-entity-graph",
        attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genres")})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Author.class)
    @JoinColumn(name = "author_id")
    private Author author;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY,
            targetEntity = Genre.class)
    @JoinTable(name = "genres_relationships", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;
}
