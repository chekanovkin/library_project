package com.project.library_project.repo;

import com.project.library_project.entity.Book;
import com.project.library_project.entity.LibraryCard;
import com.project.library_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryCardRepository extends JpaRepository<LibraryCard, Long> {

    List<LibraryCard> findAllByOwner(User user);

    LibraryCard findByOwnerAndBook(User user, Book book);
}
