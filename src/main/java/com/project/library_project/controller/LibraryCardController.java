package com.project.library_project.controller;

import com.project.library_project.entity.Book;
import com.project.library_project.entity.BookStorage;
import com.project.library_project.entity.User;
import com.project.library_project.repo.BookStorageRepository;
import com.project.library_project.service.BookService;
import com.project.library_project.service.LibraryCardService;
import com.project.library_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/library-card-api")
public class LibraryCardController {

    @Autowired
    LibraryCardService libraryCardService;

    @Autowired
    BookService bookService;

    @Autowired
    BookStorageRepository bookStorageRepository;

    @Autowired
    UserService userService;

    @PostMapping("/library-card")
    public ResponseEntity<String> reserveBook(@AuthenticationPrincipal User user, @RequestParam Long bookId) {
        Book reservedBook = bookService.findById(bookId);
        BookStorage bookStorage = bookStorageRepository.getById(reservedBook.getId());
        if (user.getReservedBooks() == 5) {
            return new ResponseEntity<>("Невозможно забронировать более 5 книг", HttpStatus.BAD_REQUEST);
        } else if (bookStorage.getAmount() == 0) {
            return new ResponseEntity<>("Экземпляры книги в библиотеке кончились", HttpStatus.BAD_REQUEST);
        } else {
            libraryCardService.save(user, reservedBook);
            bookStorage.setAmount(bookStorage.getAmount() - 1);
            bookStorageRepository.save(bookStorage);
            user.setReservedBooks(user.getReservedBooks() + 1);
            userService.update(user);
            return new ResponseEntity<>("Пользователь " + user.getLogin() + " забронировал книгу \"" + reservedBook.getName() + "\"", HttpStatus.OK);
        }
    }
}
