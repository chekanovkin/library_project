package com.project.library_project.service;

import com.project.library_project.entity.Author;
import com.project.library_project.repo.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    public Author findById(Long id) {
        return authorRepository.getById(id);
    }
}
