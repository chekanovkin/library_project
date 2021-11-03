package com.project.library_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library_project.entity.Book;
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
import java.util.List;
import java.util.Objects;

@Controller
//@PreAuthorize("isAuthenticated()")
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

    //@PreAuthorize("hasAuthority('LIBRARIAN')")
    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestParam(value = "book") MultipartFile file,
                                             @RequestParam() String name,
                                             @RequestParam() Integer amount,
                                             @RequestParam() Integer year,
                                             @RequestParam(required = false) String description,
                                             @RequestParam(value = "author") Long authorId) {
        storageService.uploadMultipartFile(file);
        ObjectMapper mapper = new ObjectMapper();
        Book book = new Book();
        book.setName(name);
        book.setAmount(amount);
        book.setYear(year);
        if (StringUtils.isNotEmpty(description)) {
            book.setDescription(description);
        }
        book.setAuthor(authorService.findById(authorId));
        book.setFilename(file.getOriginalFilename());
        try {
            bookService.save(book);
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
    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        Book bookToDelete = bookService.findById(id);
        bookService.delete(bookToDelete);
        storageService.deleteFile(bookToDelete.getFilename());
        return new ResponseEntity<>("Книга \"" + bookToDelete.getName() + "\" была удалена", HttpStatus.OK);
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

    @PostMapping("/byGenre")
    public ResponseEntity<String> getBooksByGenre(@RequestParam String genre) {
        ObjectMapper mapper = new ObjectMapper();
        List<Book> bookList = bookService.getAllByGenre(genre);
        try {
            return new ResponseEntity<>(mapper.writeValueAsString(bookList), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(StringUtils.EMPTY, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id,
                                             @RequestParam(value = "book", required = false) MultipartFile file,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) String description,
                                             @RequestParam(required = false) Integer amount) {
        ObjectMapper mapper = new ObjectMapper();
        Book bookToUpdate = bookService.findById(id);
        if (StringUtils.isNotEmpty(name)) {
            bookToUpdate.setName(name);
        }
        if (StringUtils.isNotEmpty(description)) {
            bookToUpdate.setDescription(description);
        }
        if (Objects.nonNull(file)) {
            bookToUpdate.setFilename(file.getOriginalFilename());
        }
        if (Objects.nonNull(amount)) {
            bookToUpdate.setName(name);
        }
        try {
            bookService.update(bookToUpdate);
            return new ResponseEntity<>(mapper.writeValueAsString(bookToUpdate), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>("Не удалось обновить книгу", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/reserve/{id}")
    public ResponseEntity<String> reserveBook(@AuthenticationPrincipal User user, @PathVariable Long id) {
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
