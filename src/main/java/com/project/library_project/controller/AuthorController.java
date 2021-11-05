package com.project.library_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library_project.entity.Author;
import com.project.library_project.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/author-api")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @PostMapping("/author")
    public ResponseEntity<String> addAuthor(@RequestParam String name,
                                            @RequestParam String surname,
                                            @RequestParam(required = false) String patronymic) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Author author = authorService.save(name, surname, patronymic);
        return new ResponseEntity<>(mapper.writeValueAsString(author), HttpStatus.OK);
    }

    @DeleteMapping("/author/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        if (authorService.delete(id)) {
            return new ResponseEntity<>("Автор с id = " + id + " был удален", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Автор с id = " + id + " не найден", HttpStatus.BAD_REQUEST);
        }
    }
}
