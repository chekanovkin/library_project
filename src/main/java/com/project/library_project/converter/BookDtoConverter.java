package com.project.library_project.converter;

import com.project.library_project.dto.BookDto;
import com.project.library_project.entity.Book;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class BookDtoConverter {

    public static BookDto convert(Book book) {
        BookDto bookDto = new BookDto();
        if (Objects.nonNull(book.getId())) {
            bookDto.setId(book.getId());
        }
        if (StringUtils.isNotEmpty(book.getName())) {
            bookDto.setName(book.getName());
        }
        if (StringUtils.isNotEmpty(book.getFilename())) {
            bookDto.setFilename(book.getFilename());
        }
        if (StringUtils.isNotEmpty(book.getDescription())) {
            bookDto.setDescription(book.getDescription());
        }

        bookDto.setYear(book.getYear());

        if (Objects.nonNull(book.getAuthors())) {
            bookDto.setAuthors(book.getAuthors());
        }
        if (Objects.nonNull(book.getLibraryCards())) {
            bookDto.setLibraryCards(book.getLibraryCards());
        }
        if (Objects.nonNull(book.getGenres())) {
            bookDto.setGenres(book.getGenres());
        }
        if (Objects.nonNull(book.getBookStorage())) {
            bookDto.setBookStorage(book.getBookStorage());
        }
        return bookDto;
    }
}
