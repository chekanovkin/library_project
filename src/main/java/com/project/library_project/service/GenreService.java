package com.project.library_project.service;

import com.project.library_project.entity.BaseEntity;
import com.project.library_project.entity.Genre;
import com.project.library_project.exception.GenreNotFoundException;
import com.project.library_project.repo.GenreRepository;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GenreService {

    @Autowired
    GenreRepository genreRepository;

    public Genre findByName(String name) {
        return genreRepository.findByName(name).orElseThrow(GenreNotFoundException::new);
    }

    public Genre findById(Long id) {
        return genreRepository.findById(id).orElseThrow(GenreNotFoundException::new);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ObjectNotFoundException.class, ConstraintViolationException.class})
    public Genre save(String name) {
        Optional<Genre> genreFromDb = genreRepository.findByName(name);
        if (genreFromDb.isPresent()) {
            Genre genre = genreFromDb.get();
            genre.setExists(true);
            return genre;
        }
        Genre genre = new Genre();
        genre.setName(name);
        genre.setExists(false);
        return genreRepository.save(genre);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ObjectNotFoundException.class, ConstraintViolationException.class})
    public Genre update(Long id, String name) {
        Genre genre = genreRepository.getById(id);
        genre.setName(name);
        return genreRepository.save(genre);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ObjectNotFoundException.class, ConstraintViolationException.class})
    public Genre delete(Long id) {
        Genre genre = findById(id);
        genreRepository.delete(genre);
        return genre;
    }
}
