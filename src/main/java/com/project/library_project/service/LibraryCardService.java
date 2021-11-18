package com.project.library_project.service;

import com.project.library_project.entity.Book;
import com.project.library_project.entity.LibraryCard;
import com.project.library_project.entity.User;
import com.project.library_project.repo.LibraryCardRepository;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class LibraryCardService {

    @Autowired
    LibraryCardRepository cardRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ObjectNotFoundException.class, ConstraintViolationException.class})
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
