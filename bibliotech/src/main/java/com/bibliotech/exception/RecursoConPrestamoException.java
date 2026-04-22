package com.bibliotech.exception;

public class RecursoConPrestamoException extends PrestamoException {
    public RecursoConPrestamoException() {
        super("Recuso con prestamo activo");
    }
    public RecursoConPrestamoException(String message) {
        super(message);
    }
}
