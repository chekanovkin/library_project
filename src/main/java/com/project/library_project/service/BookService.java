package com.project.library_project.service;

import com.project.library_project.entity.Book;
import com.project.library_project.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
