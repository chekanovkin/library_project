package com.project.library_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library_project.entity.Genre;
import com.project.library_project.service.GenreService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
@RequestMapping("/genre-api")
public class GenreController {

    private static Logger log = Logger.getLogger(GenreController.class.getName());

    @Autowired
    GenreService genreService;

    @PostMapping("/genre")
    public ResponseEntity<String> addGenre(@RequestParam String name) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Pair<String, Genre> answer = genreService.save(name);
        if (answer.getKey().equals("Exists")) {
            log.info("Попытка создать существующий жанр " + "[" + answer.getValue().getId() + ", " + answer.getValue().getName() + "]");
            return new ResponseEntity<>("Жанр уже существует\n" + mapper.writeValueAsString(answer.getValue()), HttpStatus.OK);
        } else {
            log.info("Создан новый жанр " + "[" + answer.getValue().getId() + ", " + answer.getValue().getName() + "]");
            return new ResponseEntity<>("Создан новый жанр\n" + mapper.writeValueAsString(answer.getValue()), HttpStatus.OK);
        }
    }

    @PutMapping("/genre/{id}")
    public ResponseEntity<String> updateGenre(@PathVariable Long id, @RequestParam String name) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Genre genre = genreService.findById(id);
        Genre updatedGenre = genreService.update(id, name);
        log.info("Обновлен жанр:\nСтарая сущность: " + "[" + genre.getId() + ", " + genre.getName() + "]\nНовая сущность: " + "[" + updatedGenre.getId() + ", " + updatedGenre.getName() + "]");
        return new ResponseEntity<>(mapper.writeValueAsString(updatedGenre), HttpStatus.OK);
    }

    @DeleteMapping("/author/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable Long id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Genre genre = genreService.delete(id);
        log.info("Удален жанр " + "[" + genre.getId() + ", " + genre.getName() + "]");
        return new ResponseEntity<>("Жанр \"" + mapper.writeValueAsString(genre) + "\" был удален", HttpStatus.OK);
    }
}
