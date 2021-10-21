package com.project.library_project.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    String name;

    @NotEmpty
    int amount;

    @NotEmpty
    int year;

    String filename;

    String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "book_genre",
        joinColumns = {@JoinColumn(name = "book_id")},
        inverseJoinColumns = {@JoinColumn(name = "genre_id")}
    )
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    private Set<LibraryCard> libraryCards = new HashSet<>();
}
