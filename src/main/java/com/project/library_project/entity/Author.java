package com.project.library_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = "books")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    String name;

    @NotEmpty
    String surname;

    @NotEmpty
    String patronymic;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "book_author",
        joinColumns = {@JoinColumn(name = "author_id")},
        inverseJoinColumns = {@JoinColumn(name = "book_id")}
    )
    @JsonIgnore
    private Set<Book> books = new HashSet<>();
}
