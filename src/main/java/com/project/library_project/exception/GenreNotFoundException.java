package com.project.library_project.exception;

public class GenreNotFoundException extends RuntimeException {

    public GenreNotFoundException() {
        super("Жанр не найден");
    }
}
