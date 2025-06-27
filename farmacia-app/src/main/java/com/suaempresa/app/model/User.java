package com.suaempresa.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Anotação do Lombok para gerar getters, setters, toString, etc.
@NoArgsConstructor // Construtor sem argumentos
public class User {
    private String name;
    private String email;
    private String passwordHash;
    private String role; // "user" ou "admin"
}