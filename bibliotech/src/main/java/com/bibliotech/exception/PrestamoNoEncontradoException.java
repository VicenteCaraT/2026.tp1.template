package com.bibliotech.exception;

public class PrestamoNoEncontradoException extends PrestamoException {
    public PrestamoNoEncontradoException() {
        super("Prestamo no encontrado");
    }
    public PrestamoNoEncontradoException(String message) {
        super(message);
    }
}
