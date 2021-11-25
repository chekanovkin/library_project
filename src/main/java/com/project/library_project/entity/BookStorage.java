package com.project.library_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @MapsId
    @JoinColumn(name = "book_id")
    @JsonIgnore
    private Book book;
}
