package com.project.library_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library_project.entity.Book;
import com.project.library_project.service.BookService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rabbit-api")
public class RabbitController {

    @Autowired
    RabbitTemplate template;

    @Autowired
    BookService bookService;

    @GetMapping("/process/book/{id}")
    ResponseEntity<String> sendMessage(@PathVariable() Long id) throws JsonProcessingException {
        Book book = bookService.findById(id);
        ObjectMapper mapper = new ObjectMapper();
        String bookToJson = mapper.writeValueAsString(book);
        String response = (String) template.convertSendAndReceive("query-example", bookToJson);
        return new ResponseEntity<>("Ответ от библиотеки : " + response, HttpStatus.OK);
    }
}
