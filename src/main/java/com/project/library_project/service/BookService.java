package com.project.library_project.service;

import com.project.library_project.entity.Book;
import com.project.library_project.entity.LibraryCard;
import com.project.library_project.entity.User;
import com.project.library_project.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAllByGenre(String genre) {
        return bookRepository.findBooksByGenre(genre);
    }

    public Book findById(Long id) {
        return bookRepository.getById(id);
    }

    public boolean save(Book book) {
        Book bookFromDb = bookRepository.findByNameAndAuthor(book.getName(), book.getAuthor());
        if (Objects.nonNull(bookFromDb)) {
            return false;
        }
        bookRepository.save(book);
        return true;
    }

    public boolean update(Book book) {
        Book bookFromDb = bookRepository.findByNameAndAuthor(book.getName(), book.getAuthor());
        if (Objects.isNull(bookFromDb)) {
            return false;
        }
        bookRepository.save(book);
        return true;
    }
}
