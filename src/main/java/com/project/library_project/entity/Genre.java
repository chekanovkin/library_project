package com.project.library_project.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "book_genre",
        joinColumns = {@JoinColumn(name = "genre_id")},
        inverseJoinColumns = {@JoinColumn(name = "book_id")}
    )
    private Set<Book> books = new HashSet<>();
}
