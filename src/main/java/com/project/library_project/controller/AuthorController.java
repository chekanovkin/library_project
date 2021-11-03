package com.project.library_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library_project.entity.Author;
import com.project.library_project.service.AuthorService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/add")
    public ResponseEntity<String> addAuthor(@RequestParam String name,
                                            @RequestParam String surname,
                                            @RequestParam(required = false) String patronymic) {
        ObjectMapper mapper = new ObjectMapper();
        Author author = new Author();
        author.setName(name);
        author.setSurname(surname);
        if (StringUtils.isNotEmpty(patronymic)) {
            author.setPatronymic(patronymic);
        }
        try {
            authorService.save(author);
            return new ResponseEntity<>(mapper.writeValueAsString(author), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>("Не удалось добавить автора", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        Author authorToDelete = authorService.findById(id);
        authorService.delete(authorToDelete);
        return new ResponseEntity<>("Автор \"" + authorToDelete.getName() + "\" был удален", HttpStatus.OK);
    }
}
