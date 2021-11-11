package com.project.library_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library_project.entity.Genre;
import com.project.library_project.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/genre-api")
public class GenreController {

    @Autowired
    GenreService genreService;

    @PostMapping("/genre")
    public ResponseEntity<String> addGenre(@RequestParam String name) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Genre genre = genreService.save(name);
        return new ResponseEntity<>(mapper.writeValueAsString(genre), HttpStatus.OK);
    }

    @DeleteMapping("/author/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable Long id) {
        if (genreService.delete(id)) {
            return new ResponseEntity<>("Жанр с id = " + id + " был удален", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Жанр с id = " + id + " не найден", HttpStatus.BAD_REQUEST);
        }
    }
}
