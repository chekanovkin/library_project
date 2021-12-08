package com.project.library_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library_project.converter.BookDtoConverter;
import com.project.library_project.dto.BookDto;
import com.project.library_project.entity.Book;
import com.project.library_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Controller
//@PreAuthorize("isAuthenticated()")
@RequestMapping("/books-api")
public class BookController {

    private static Logger log = Logger.getLogger(BookController.class.getName());

    @Autowired
    StorageService storageService;

    @Autowired
    BookService bookService;

    //@PreAuthorize("hasAuthority('LIBRARIAN')")
    @PostMapping("/book")
    public ResponseEntity<String> addBook(@RequestParam() String name,
                                          @RequestParam() Integer amount,
                                          @RequestParam() Integer year,
                                          @RequestParam(required = false) String description,
                                          @RequestParam(value = "author") Set<Long> authorId,
                                          @RequestParam Set<String> genres) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Book book = bookService.save(name, amount, year, description, authorId, genres);
        if (book.isExists()) {
            log.info("Попытка создать существующую книгу " + "[" + book.getId() + ", " + book.getName() + "]");
            return new ResponseEntity<>("Книга уже существует", HttpStatus.CONFLICT);
        } else {
            log.info("Создана новая книга " + "[" + book.getId() + ", " + book.getName() + "]");
            return new ResponseEntity<>("Книга добавлена\n" + mapper.writeValueAsString(book), HttpStatus.OK);
        }
    }

    @GetMapping("/book/{filename}")
    public ResponseEntity<ByteArrayResource> readBook(@PathVariable String filename) {
        ByteArrayResource resource = new ByteArrayResource(storageService.downloadFile(filename));
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
            .contentType(MediaType.APPLICATION_PDF)
            .contentLength(resource.contentLength())
            .body(resource);
    }

    //@PreAuthorize("hasAuthority('LIBRARIAN')")
    @DeleteMapping("/book/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        String name = bookService.delete(id);
        log.info("Удалена книга " + "[" + id + ", " + name + "]");
        return new ResponseEntity<>("Книга \"" + name + "\" была удалена", HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/books")
    public ResponseEntity<String> getDeleted() throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        return new ResponseEntity<>(mapper.writeValueAsString(bookService.findAllDeleted(true)), HttpStatus.OK);
    }

    @GetMapping("/books")
    public ResponseEntity<String> getBookList() throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        return new ResponseEntity<>(mapper.writeValueAsString(bookService.getAllBooks(false)), HttpStatus.OK);
    }

    @GetMapping("/books/by-genre")
    public ResponseEntity<String> getBooksByGenre(@RequestParam String genre) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Book> bookList = bookService.getAllByGenre(genre);
        return new ResponseEntity<>(mapper.writeValueAsString(bookList), HttpStatus.OK);
    }

    @GetMapping("/books/by-author")
    public ResponseEntity<String> getBooksByAuthor(@RequestParam String author) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Book> bookList = bookService.getAllByAuthor(author);
        return new ResponseEntity<>(mapper.writeValueAsString(bookList), HttpStatus.OK);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id,
                                             @RequestParam(value = "book", required = false) MultipartFile file,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) Integer amount,
                                             @RequestParam(required = false) String description,
                                             @RequestParam(required = false) Set<Long> authorIds,
                                             @RequestParam(required = false) Set<String> genres) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Book book = bookService.findById(id);
        BookDto bookDto = BookDtoConverter.convert(book);
        Book bookToUpdate = bookService.update(id, file, amount, name, description, authorIds, genres);
        log.info("Книга обновлена:\nСтарая сущность: " + "[" + bookDto.getId() + ", " + bookDto.getName() + "]\nНовая сущность: " + "[" + book.getId() + ", " + book.getName() + "]");
        return new ResponseEntity<>(mapper.writeValueAsString(bookToUpdate), HttpStatus.OK);
    }
}
