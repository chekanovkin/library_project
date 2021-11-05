package com.project.library_project.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = "books")
//@ToString(exclude = "books")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    @JsonManagedReference
    private Set<Book> books = new HashSet<>();
}
