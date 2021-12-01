package com.project.library_project.aspect;

import com.project.library_project.entity.Author;
import com.project.library_project.entity.BaseEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LogAspect {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Pointcut("execution(public * com.project.library_project.service.AuthorService.*(..))")
    public void callAtAuthorServicePublic() { }

    @Before("callAtAuthorServicePublic()")
    public void beforeCallAtAuthorServiceMethod(JoinPoint jp) {
        logger.info(jp.toString());
    }

    @Pointcut("execution(public * com.project.library_project.service.AuthorService.save(..)) && args(name, surname, patronymic)")
    public void callAtAuthorServiceSave(String name, String surname, String patronymic) { }

    @AfterReturning(pointcut = "callAtAuthorServiceSave(String, String, String)", returning = "answer")
    public void afterCallSave(BaseEntity answer) {
        Author author = (Author) answer.getEntity();
        if (answer.isExists()) {
            logger.info("Попытка создать существующего автора " + "[" +  author.getId() + ", " + author.getSurname() + " " + author.getName() + " " + author.getPatronymic() + "]");
        } else {
            logger.info("Создан новый автор " + "[" + author.getId() + ", " + author.getSurname() + " " + author.getName() + " " + author.getPatronymic() + "]");
        }
    }

    @Pointcut("execution(public * com.project.library_project.service.AuthorService.update(..)) && args(id, name, surname, patronymic)")
    public void callAtAuthorServiceUpdate(Long id, String name, String surname, String patronymic) { }

    @AfterReturning(pointcut = "callAtAuthorServiceUpdate(Long, String, String, String)", returning = "updatedAuthor")
    public void afterCallUpdate(Author updatedAuthor) {
        logger.info("Обновлен автор: [" + updatedAuthor.getId() + ", " + updatedAuthor.getSurname() + " " + updatedAuthor.getName() + " " + updatedAuthor.getPatronymic() + "]");
    }

    @Pointcut("execution(public * com.project.library_project.service.AuthorService.delete(..)) && args(id)")
    public void callAtAuthorServiceDelete(Long id) { }

    @AfterReturning(pointcut = "callAtAuthorServiceDelete(Long)", returning = "author")
    public void afterCallDelete(Author author) {
        logger.info("Удален автор " + "[" + author.getId() + ", " + author.getSurname() + author.getName() + author.getPatronymic() + "]");
    }
}
