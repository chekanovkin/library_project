package com.project.library_project.repo;

import com.project.library_project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByName(String name);

    List<Book> findByYear(int year);

    @Query("from Book b " +
        "join b.genres g " +
        "where lower(concat(g.name)) like lower(concat('%', :genre, '%'))")
    List<Book> findBooksByGenre(String genre);
}
