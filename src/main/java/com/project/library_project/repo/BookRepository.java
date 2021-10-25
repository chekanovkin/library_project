package com.project.library_project.repo;

import com.project.library_project.entity.Author;
import com.project.library_project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByNameAndAuthor(String name, Author author);

    List<Book> findByYear(int year);

    @Query("from Book b " +
        "join b.genres g " +
        "where lower(concat(g.name)) like lower(concat('%', :genre, '%'))")
    List<Book> findBooksByGenre(String genre);
}
