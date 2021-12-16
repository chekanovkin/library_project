package com.project.library_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.project.library_project.entity.Book;
import com.project.library_project.service.BookService;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate.RabbitConverterFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.logging.Logger;

@Controller
@RequestMapping("/rabbit-api")
public class RabbitController {

    private static Logger log = Logger.getLogger(RabbitController.class.getName());

    @Autowired
    AsyncRabbitTemplate template;

    @Autowired
    BookService bookService;

    @GetMapping("/process/book/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void sendMessage(@PathVariable() Long id) throws JsonProcessingException {
        Book book = bookService.findById(id);
        SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
            .serializeAllExcept("id");
        FilterProvider filters = new SimpleFilterProvider()
            .addFilter("myFilter", theFilter);
        ObjectMapper mapper = new ObjectMapper();
        String bookToJson = mapper.writer(filters).writeValueAsString(book);
        RabbitConverterFuture<String> response = template.convertSendAndReceive("query-example", bookToJson);
        response.addCallback(
            sampleResponseMessage ->
                log.info("Ответ библиотеки: " + sampleResponseMessage)
            , failure ->
                log.info(failure.getMessage())
        );
    }
}
