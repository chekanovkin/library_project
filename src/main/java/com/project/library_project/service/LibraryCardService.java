package com.project.library_project.service;

import com.project.library_project.entity.LibraryCard;
import com.project.library_project.repo.LibraryCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LibraryCardService {

    @Autowired
    LibraryCardRepository cardRepository;

    public boolean save(LibraryCard card) {
        LibraryCard cardFromDb = cardRepository.findByOwnerAndBook(card.getOwner(), card.getBook());
        if (Objects.nonNull(cardFromDb)) {
            return false;
        }
        cardRepository.save(card);
        return true;
    }

    public boolean update(LibraryCard card) {
        LibraryCard cardFromDb = cardRepository.findByOwnerAndBook(card.getOwner(), card.getBook());
        if (Objects.isNull(cardFromDb)) {
            return false;
        }
        cardRepository.save(card);
        return true;
    }
}
