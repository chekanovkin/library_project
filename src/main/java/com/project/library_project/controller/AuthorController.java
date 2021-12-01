package com.project.library_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library_project.entity.Author;
import com.project.library_project.entity.BaseEntity;
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
        BaseEntity answer = authorService.save(name, surname, patronymic);
        if (answer.isExists()) {
            return new ResponseEntity<>("Автор уже существует", HttpStatus.CONFLICT);
        } else {
            Author author = (Author) answer.getEntity();
            return new ResponseEntity<>("Создан новый автор\n" + mapper.writeValueAsString(author), HttpStatus.OK);
        }
    }

    @PutMapping("/author/{id}")
    public ResponseEntity<String> updateAuthor(@PathVariable Long id,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) String surname,
                                               @RequestParam(required = false) String patronymic) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Author updatedAuthor = authorService.update(id, name, surname, patronymic);
        return new ResponseEntity<>(mapper.writeValueAsString(updatedAuthor), HttpStatus.OK);
    }

    @DeleteMapping("/author/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Author author = authorService.delete(id);
        return new ResponseEntity<>("Автор \"" + mapper.writeValueAsString(author) + "\" был удален", HttpStatus.OK);
    }
}
