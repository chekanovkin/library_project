package com.project.library_project.repo;

import com.project.library_project.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Set<Author> findBySurname(String surname);

    Author findByNameAndSurname(String name, String surname);
}
