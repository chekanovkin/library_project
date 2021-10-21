package com.project.library_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library_project.service.BookService;
import com.project.library_project.service.StorageService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    StorageService storageService;

    @Autowired
    BookService bookService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadBook(@RequestParam(value = "book") MultipartFile file) {
        return new ResponseEntity<>(storageService.uploadMultipartFile(file), HttpStatus.OK);
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
}
