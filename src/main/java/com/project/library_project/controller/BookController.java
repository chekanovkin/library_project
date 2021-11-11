package com.project.library_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library_project.entity.Book;
import com.project.library_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Controller
//@PreAuthorize("isAuthenticated()")
@RequestMapping("/books-api")
public class BookController {

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
                                          @RequestParam List<String> genres) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Book book = bookService.save(name, amount, year, description, authorId, genres);
        return new ResponseEntity<>(mapper.writeValueAsString(book), HttpStatus.OK);
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
        return new ResponseEntity<>("Книга \"" + name + "\" была удалена", HttpStatus.OK);
    }

    @GetMapping("/books")
    public ResponseEntity<String> getBookList() throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        return new ResponseEntity<>(mapper.writeValueAsString(bookService.getAllBooks()), HttpStatus.OK);
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
                                             @RequestParam(required = false) String description,
                                             @RequestParam(required = false) Integer amount) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Book bookToUpdate = bookService.update(id, file, name, description, amount);
        return new ResponseEntity<>(mapper.writeValueAsString(bookToUpdate), HttpStatus.OK);
    }
}
