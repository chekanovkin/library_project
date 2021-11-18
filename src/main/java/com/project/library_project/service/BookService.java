package com.project.library_project.service;

import com.project.library_project.entity.Book;
import com.project.library_project.entity.BookStorage;
import com.project.library_project.entity.Genre;
import com.project.library_project.exception.BookNotFoundException;
import com.project.library_project.repo.BookRepository;
import com.project.library_project.repo.BookStorageRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorService authorService;

    @Autowired
    StorageService storageService;

    @Autowired
    GenreService genreService;

    @Autowired
    BookStorageRepository bookStorageRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAllByGenre(String genre) {
        return bookRepository.findBooksByGenre(genre);
    }

    public List<Book> getAllByAuthor(String author) {
        return bookRepository.findBooksByAuthors_Name(author);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ObjectNotFoundException.class, ConstraintViolationException.class})
    public Book save(String name, Integer amount, Integer year, String description, Set<Long> authorIds, List<String> genreNames) {
        Book bookFromDb = bookRepository.findByNameAndAuthorsIn(name, authorService.findByIdIn(authorIds));
        if (Objects.nonNull(bookFromDb)) {
            return bookFromDb;
        }
        Book book = new Book();
        book.setName(name);
        book.setYear(year);
        if (StringUtils.isNotEmpty(description)) {
            book.setDescription(description);
        }
        book.setAuthors(authorService.findByIdIn(authorIds));
        Set<Genre> genres = new HashSet<>();
        for (String str : genreNames) {
            Genre genre = genreService.findByName(str);
            genres.add(genre);
        }
        book.setGenres(genres);
        bookRepository.save(book);
        BookStorage bookStorage = new BookStorage();
        bookStorage.setBook(book);
        bookStorage.setAmount(amount);
        bookStorageRepository.save(bookStorage);
        return bookRepository.findByNameAndAuthorsIn(book.getName(), book.getAuthors());
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ObjectNotFoundException.class, ConstraintViolationException.class})
    public Book update(Long id, MultipartFile file, String name, String description) {
        Book bookToUpdate = findById(id);
        if (StringUtils.isNotEmpty(name)) {
            bookToUpdate.setName(name);
        }
        if (StringUtils.isNotEmpty(description)) {
            bookToUpdate.setDescription(description);
        }
        if (Objects.nonNull(file)) {
            storageService.deleteFile(bookToUpdate.getFilename());
            storageService.uploadMultipartFile(file);
            bookToUpdate.setFilename(file.getOriginalFilename());
        }
        return bookRepository.save(bookToUpdate);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ObjectNotFoundException.class, ConstraintViolationException.class})
    public String delete(Long id) {
        Book book = findById(id);
        storageService.deleteFile(book.getFilename());
        bookRepository.delete(book);
        return book.getName();
    }
}
