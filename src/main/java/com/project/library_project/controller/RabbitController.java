package com.project.library_project.controller;

import com.project.library_project.entity.Book;
import com.project.library_project.service.BookService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rabbit-api")
public class RabbitController {

    @Autowired
    RabbitTemplate template;

    @Autowired
    BookService bookService;

    @RequestMapping("/process/book/{id}")
    ResponseEntity<String> sendMessage(@PathVariable("book") Long id) {
        Book book = bookService.findById(id);
        String response = (String) template.convertSendAndReceive("query-example", book);
        return new ResponseEntity<>("Ответ от библиотеки : " + response, HttpStatus.OK);
    }
}
