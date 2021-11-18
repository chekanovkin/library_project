package com.project.library_project.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
public class BookStorage {

    @Id
    @Column(name = "book_id")
    private Long id;

    @NotEmpty
    private int amount;

    @OneToOne()
    @MapsId
    @JoinColumn(name = "book_id")
    private Book book;
}
