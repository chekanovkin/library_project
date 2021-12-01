package com.project.library_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    @JsonIgnore
    private boolean exists;

    @JsonIgnore
    private BaseEntity entity;
}
