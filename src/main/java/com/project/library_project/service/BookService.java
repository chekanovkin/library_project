package com.project.library_project.service;

import com.project.library_project.entity.Book;
import com.project.library_project.entity.Genre;
import com.project.library_project.exception.BookNotFoundException;
import com.project.library_project.repo.BookRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAllByGenre(String genre) {
        return bookRepository.findBooksByGenre(genre);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    public Book save(String name, Integer amount, Integer year, String description, Long authorId, List<String> genreNames) {
        Book bookFromDb = bookRepository.findByNameAndAuthor(name, authorService.findById(authorId));
        if (Objects.nonNull(bookFromDb)) {
            return bookFromDb;
        }
        Book book = new Book();
        book.setName(name);
        book.setAmount(amount);
        book.setYear(year);
        if (StringUtils.isNotEmpty(description)) {
            book.setDescription(description);
        }
        book.setAuthor(authorService.findById(authorId));
        Set<Genre> genres = new HashSet<>();
        for (String str : genreNames) {
            Genre genre = genreService.findByName(str);
            genres.add(genre);
        }
        book.setGenres(genres);
        bookRepository.save(book);
        return bookRepository.findByNameAndAuthor(book.getName(), book.getAuthor());
    }

    public Book update(Long id, MultipartFile file, String name, String description, Integer amount) {
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
        if (Objects.nonNull(amount)) {
            bookToUpdate.setName(name);
        }
        bookRepository.save(bookToUpdate);
        return bookToUpdate;
    }

    public Book update(Book book) {
        bookRepository.save(book);
        return book;
    }

    public String delete(Long id) {
        Book book = findById(id);
        storageService.deleteFile(book.getFilename());
        bookRepository.delete(book);
        return book.getName();
    }
}
