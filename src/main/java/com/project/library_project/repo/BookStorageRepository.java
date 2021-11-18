package com.project.library_project.repo;

import com.project.library_project.entity.BookStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStorageRepository extends JpaRepository<BookStorage, Long> {
}
