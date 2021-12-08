package com.project.library_project.controller;

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

    @RabbitListener(queues = "query-example")
    public String getBookFromQueue(Book book) throws InterruptedException {
        Book book1 = bookRepository.findByNameAndAuthorsIn(book.getName(), book.getAuthors());
        if (Objects.nonNull(book1)) {
            bookRepository.save(book1);
            return "Получена новая книга : " + book;
        }
        return "Отклонено, книга уже имеется";
    }
}
