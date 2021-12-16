package com.project.library_project.repo;

import com.project.library_project.entity.Author;
import com.project.library_project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByName(String name);

    Book findByNameAndAuthorsIn(String name, Set<Author> authors);

    List<Book> findByYear(int year);

    @Query("from Book b " +
        "join b.genres g " +
        "where lower(concat(g.name)) like lower(concat('%', :genre, '%'))")
    List<Book> findBooksByGenre(String genre);

    List<Book> findBooksByAuthors_Name(String authorName);
}
