package com.project.library_project.controller;

import com.amazonaws.services.dynamodbv2.xspec.B;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library_project.entity.Book;
import com.project.library_project.entity.Genre;
import com.project.library_project.entity.LibraryCard;
import com.project.library_project.entity.User;
import com.project.library_project.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/books")
public class BookController {

    @Autowired
    StorageService storageService;

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @Autowired
    LibraryCardService libraryCardService;

    @Autowired
    AuthorService authorService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @PostMapping("/add")
    public ResponseEntity<String> uploadBook(@RequestParam(value = "book") MultipartFile file,
        @RequestParam(value = "name") String name,
        @RequestParam(value = "amount") Integer amount,
        @RequestParam(value = "year") Integer year,
        @RequestParam(value = "author") Long authorId) {
        storageService.uploadMultipartFile(file);
        ObjectMapper mapper = new ObjectMapper();
        Book book = new Book();
        book.setName(name);
        book.setAmount(amount);
        book.setYear(year);
        book.setAuthor(authorService.findById(authorId));
        book.setFilename(file.getOriginalFilename());
        bookService.save(book);
        try {
            return new ResponseEntity<>(mapper.writeValueAsString(book), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>("Не удалось добавить книгу", HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/read/{filename}")
    public ResponseEntity<ByteArrayResource> readBook(@PathVariable String filename) {
        ByteArrayResource resource = new ByteArrayResource(storageService.downloadFile(filename));
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
            .contentType(MediaType.APPLICATION_PDF)
            .contentLength(resource.contentLength())
            .body(resource);
    }

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<String> deleteBook(@PathVariable String filename) {
        return new ResponseEntity<>(storageService.deleteFile(filename), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<String> getBookList() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new ResponseEntity<>(mapper.writeValueAsString(bookService.getAllBooks()), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(StringUtils.EMPTY, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/list/byGenre")
    public ResponseEntity<String> getBooksByGenre(String genre) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new ResponseEntity<>(mapper.writeValueAsString(bookService.getAllByGenre(genre)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(StringUtils.EMPTY, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveBook(@AuthenticationPrincipal User user, @RequestParam Long id) {
        Book reservedBook = bookService.findById(id);
        if (user.getReservedBooks() == 5) {
            return new ResponseEntity<>("Невозможно забронировать более 5 книг", HttpStatus.BAD_REQUEST);
        } else if (reservedBook.getAmount() == 0) {
            return new ResponseEntity<>("Экземпляры книги в библиотеке кончились", HttpStatus.BAD_REQUEST);
        } else {
            LibraryCard card = new LibraryCard();
            card.setOwner(user);
            card.setBook(reservedBook);
            card.setReceivingDate(LocalDate.now());
            card.setDeliveryDate(LocalDate.now().plusWeeks(2));
            card.setOwner(user);
            card.setBook(reservedBook);
            libraryCardService.save(card);
            reservedBook.setAmount(reservedBook.getAmount() - 1);
            bookService.update(reservedBook);
            user.setReservedBooks(user.getReservedBooks() + 1);
            userService.update(user);
            return new ResponseEntity<>("Пользователь " + user.getLogin() + " забронировал книгу \"" + reservedBook.getName() + "\"", HttpStatus.OK);
        }
    }
}
