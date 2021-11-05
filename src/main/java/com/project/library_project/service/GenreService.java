package com.project.library_project.service;

import com.project.library_project.entity.Genre;
import com.project.library_project.exception.GenreNotFoundException;
import com.project.library_project.repo.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreService {

    @Autowired
    GenreRepository genreRepository;

    public Genre findByName(String name) {
        return genreRepository.findByName(name).orElseThrow(GenreNotFoundException::new);
    }
}
