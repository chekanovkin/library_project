package com.project.library_project.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
@JsonFilter("myFilter")
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Transient
    private boolean exists;
}
