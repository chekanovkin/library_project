package com.project.library_project.service;

import com.project.library_project.entity.Book;
import com.project.library_project.entity.LibraryCard;
import com.project.library_project.entity.User;
import com.project.library_project.repo.LibraryCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class LibraryCardService {

    @Autowired
    LibraryCardRepository cardRepository;

    public boolean save(User user, Book reservedBook) {
        LibraryCard cardFromDb = cardRepository.findByOwnerAndBook(user, reservedBook);
        if (Objects.nonNull(cardFromDb)) {
            return false;
        }
        LibraryCard card = new LibraryCard();
        card.setOwner(user);
        card.setBook(reservedBook);
        card.setReceivingDate(LocalDate.now());
        card.setDeliveryDate(LocalDate.now().plusWeeks(2));
        card.setOwner(user);
        card.setBook(reservedBook);
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
