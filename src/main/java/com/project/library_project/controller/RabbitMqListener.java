package com.project.library_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library_project.entity.Book;
import com.project.library_project.repo.BookRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RabbitMqListener {

    @Autowired
    BookRepository bookRepository;

    @RabbitListener(queues = "query-example-2")
    public String getBookFromQueue(String bookInJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Book book = mapper.readValue(bookInJson, Book.class);
        Book bookInDb = bookRepository.findByName(book.getName());
        if (Objects.isNull(bookInDb)) {
            book.getBookStorage().setBook(book);
            bookRepository.save(book);
            return "Получена новая книга : " + book;
        }
        return "Отклонено, книга уже имеется";
    }
}
