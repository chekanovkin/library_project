package com.project.library_project.service;

import com.project.library_project.entity.Author;
import com.project.library_project.entity.Book;
import com.project.library_project.exception.AuthorNotFoundException;
import com.project.library_project.repo.AuthorRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    public Author findById(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public Author save(String name, String surname, String patronymic) {
        Author authorFromDb = authorRepository.findByNameAndSurname(name, surname);
        if (Objects.nonNull(authorFromDb)) {
            return authorFromDb;
        }
        Author author = new Author();
        author.setName(name);
        author.setSurname(surname);
        if (StringUtils.isNotEmpty(patronymic)) {
            author.setPatronymic(patronymic);
        }
        authorRepository.save(author);
        return authorRepository.findByNameAndSurname(name, surname);
    }

    public boolean delete(Long id) {
        Optional<Author> authorFromDb = authorRepository.findById(id);
        if (authorFromDb.isEmpty()) {
            return false;
        }
        authorRepository.delete(authorFromDb.get());
        return true;
    }
}
