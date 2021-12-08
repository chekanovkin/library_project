package com.project.library_project.dto;

import com.project.library_project.entity.Author;
import com.project.library_project.entity.BookStorage;
import com.project.library_project.entity.Genre;
import com.project.library_project.entity.LibraryCard;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class BookDto {

    private Long id;

    String name;

    int year;

    String filename;

    String description;

    private boolean deleted = Boolean.FALSE;

    private BookStorage bookStorage;

    private Set<Author> authors;

    private Set<Genre> genres = new HashSet<>();

    private Set<LibraryCard> libraryCards = new HashSet<>();
}
