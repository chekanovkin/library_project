package com.project.library_project.service;

import com.project.library_project.entity.Genre;
import com.project.library_project.exception.GenreNotFoundException;
import com.project.library_project.repo.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class GenreService {

    @Autowired
    GenreRepository genreRepository;

    public Genre findByName(String name) {
        return genreRepository.findByName(name).orElseThrow(GenreNotFoundException::new);
    }

    public Genre save(String name) {
        Genre genreFromDb = findByName(name);
        if (Objects.nonNull(genreFromDb)) {
            return genreFromDb;
        }
        Genre genre = new Genre();
        genre.setName(name);
        return findByName(genre.getName());
    }

    public boolean delete(Long id) {
        Optional<Genre> genreFromDb = genreRepository.findById(id);
        if (genreFromDb.isEmpty()) {
            return false;
        }
        genreRepository.delete(genreFromDb.get());
        return true;
    }
}
