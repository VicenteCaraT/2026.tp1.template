package com.bibliotech.service;

import com.bibliotech.exception.ValidacionException;

public class Validador {

    // Email
    public static void validarEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidacionException("Email invalido " + email);
        }
    }

    // ISBN
    public static void validarIsbn(String isbn) {
        if(isbn == null || !isbn.matches("\\d{13}")) {
            throw new ValidacionException("ISBN invalido, debe tener 13 digito s" + isbn);
        }
    }

    //DNI
    public static void validarDni(String dni) {
        if (dni == null || !dni.matches("\\d{7,8}")) {
            throw new ValidacionException("DNI invalido, debe tener 7 u 8 dígitos " + dni);
        }
    }

    // Nombre
    public static void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty() || !nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            throw new ValidacionException("Nombre inválido: " + nombre);
        }
    }
}
