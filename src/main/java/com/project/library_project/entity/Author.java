package com.project.library_project.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
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
    private Set<Book> projectTasks = new HashSet<>();
}
