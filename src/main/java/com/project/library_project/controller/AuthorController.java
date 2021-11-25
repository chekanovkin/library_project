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

import java.util.AbstractMap;
import java.util.logging.Logger;

@Controller
@RequestMapping("/author-api")
public class AuthorController {

    private static Logger log = Logger.getLogger(AuthorController.class.getName());

    @Autowired
    AuthorService authorService;

    @PostMapping("/author")
    public ResponseEntity<String> addAuthor(@RequestParam String name,
                                            @RequestParam String surname,
                                            @RequestParam(required = false) String patronymic) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        AbstractMap.SimpleEntry<String, Author> answer = authorService.save(name, surname, patronymic);
        if (answer.getKey().equals("Exists")) {
            log.info("Попытка создать существующего автора " + "[" + answer.getValue().getId() + ", " + answer.getValue().getSurname() + answer.getValue().getName() + answer.getValue().getPatronymic() + "]");
            return new ResponseEntity<>("Автор уже существует\n" + mapper.writeValueAsString(answer.getValue()), HttpStatus.OK);
        } else {
            log.info("Создан новый автор " + "[" + answer.getValue().getId() + ", " + answer.getValue().getSurname() + answer.getValue().getName() + answer.getValue().getPatronymic() + "]");
            return new ResponseEntity<>("Создан новый автор\n" + mapper.writeValueAsString(answer.getValue()), HttpStatus.OK);
        }
    }

    @PutMapping("/author/{id}")
    public ResponseEntity<String> updateAuthor(@PathVariable Long id,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) String surname,
                                               @RequestParam(required = false) String patronymic) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Author author = authorService.findById(id);
        Author updatedAuthor = authorService.update(id, name, surname, patronymic);
        log.info("Обновлен автор:\nСтарая сущность : " + "[" + author.getId() + ", " + author.getSurname() + author.getName() + author.getPatronymic() + "]\nНовая сущность: " + "[" + updatedAuthor.getId() + ", " + updatedAuthor.getSurname() + updatedAuthor.getName() + updatedAuthor.getPatronymic() + "]");
        return new ResponseEntity<>(mapper.writeValueAsString(updatedAuthor), HttpStatus.OK);
    }

    @DeleteMapping("/author/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Author author = authorService.delete(id);
        log.info("Удален автор " + "[" + author.getId() + ", " + author.getSurname() + author.getName() + author.getPatronymic() + "]");
        return new ResponseEntity<>("Автор \"" + mapper.writeValueAsString(author) + "\" был удален", HttpStatus.OK);
    }
}
