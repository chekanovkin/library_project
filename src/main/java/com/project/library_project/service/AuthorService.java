package com.project.library_project.service;

import com.project.library_project.entity.Author;
import com.project.library_project.exception.AuthorNotFoundException;
import com.project.library_project.repo.AuthorRepository;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    public Author findById(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public Set<Author> findByIdIn(Set<Long> ids) {
        return authorRepository.findByIdIn(ids);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ObjectNotFoundException.class, ConstraintViolationException.class})
    public Pair<String, Author> save(String name, String surname, String patronymic) {
        Author authorFromDb = authorRepository.findByNameAndSurname(name, surname);
        if (Objects.nonNull(authorFromDb)) {
            return new Pair<>("Exists", authorFromDb);
        }
        Author author = new Author();
        author.setName(name);
        author.setSurname(surname);
        if (StringUtils.isNotEmpty(patronymic)) {
            author.setPatronymic(patronymic);
        }
        return new Pair<>("New", authorRepository.save(author));
    }

    public Author update(Long id, String name, String surname, String patronymic) {
        Author author = authorRepository.getById(id);
        if (Objects.nonNull(name)) {
            author.setName(name);
        }
        if (Objects.nonNull(surname)) {
            author.setSurname(surname);
        }
        if (Objects.nonNull(patronymic)) {
            author.setPatronymic(patronymic);
        }
        return authorRepository.save(author);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ObjectNotFoundException.class, ConstraintViolationException.class})
    public Author delete(Long id) {
        /*Optional<Author> authorFromDb = authorRepository.findById(id);
        if (authorFromDb.isEmpty()) {
            return false;
        }
        authorRepository.delete(authorFromDb.get());
        return true;*/
        Author author = findById(id);
        authorRepository.delete(author);
        return author;
    }
}
