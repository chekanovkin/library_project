package com.project.library_project.scheduler;

import com.project.library_project.entity.Book;
import com.project.library_project.entity.BookStorage;
import com.project.library_project.entity.LibraryCard;
import com.project.library_project.entity.User;
import com.project.library_project.repo.BookStorageRepository;
import com.project.library_project.repo.LibraryCardRepository;
import com.project.library_project.service.BookService;
import com.project.library_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class Scheduler {

    @Autowired
    UserService userService;

    @Autowired
    BookStorageRepository bookStorageRepository;

    @Autowired
    LibraryCardRepository libraryCardRepository;

    @Scheduled(fixedDelay = 60*60*1000)
    public void scheduleLibraryCards() {
        List<LibraryCard> cards = libraryCardRepository.findAll()
            .stream()
            .filter(lc -> lc.getDeliveryDate().isBefore(LocalDate.now()))
            .collect(Collectors.toList());
        libraryCardRepository.deleteAll(cards);
        List<User> users = cards.stream().map(LibraryCard::getOwner).collect(Collectors.toList());
        for (User user : users) {
            user.setReservedBooks(user.getReservedBooks() - 1);
            userService.update(user);
        }
        List<Book> books = cards.stream().map(LibraryCard::getBook).collect(Collectors.toList());
        for (Book book : books) {
            BookStorage bookStorage = bookStorageRepository.getById(book.getId());
            bookStorage.setAmount(bookStorage.getAmount() + 1);
            bookStorageRepository.save(bookStorage);
        }
    }
}
