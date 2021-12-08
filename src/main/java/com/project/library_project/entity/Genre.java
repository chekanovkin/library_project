package com.project.library_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = "books", callSuper = false)
public class Genre extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "book_genre",
        joinColumns = {@JoinColumn(name = "genre_id")},
        inverseJoinColumns = {@JoinColumn(name = "book_id")}
    )
    @JsonIgnore
    private Set<Book> books = new HashSet<>();
}
