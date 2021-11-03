package com.project.library_project.service;

import com.project.library_project.entity.Author;
import com.project.library_project.entity.Book;
import com.project.library_project.repo.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    public Author findById(Long id) {
        return authorRepository.getById(id);
    }

    public boolean save(Author author) {
        Author authorFromDb = authorRepository.findByNameAndSurname(author.getName(), author.getSurname());
        if (Objects.nonNull(authorFromDb)) {
            return false;
        }
        authorRepository.save(author);
        return true;
    }

    public boolean delete(Author author) {
        Author authorFromDb = authorRepository.findByNameAndSurname(author.getName(), author.getSurname());
        if (Objects.isNull(authorFromDb)) {
            return false;
        }
        authorRepository.delete(author);
        return true;
    }
}
