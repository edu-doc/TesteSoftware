package com.example.saltitantes.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {

    @Id
    private Long id;

    private String name;

}
