package com.bibliotech.exception;

public class RecursoNoDisponibleException extends RecursoException {
    public RecursoNoDisponibleException() {
        super("Recurso no disponible");
    }
    public RecursoNoDisponibleException(String message) {
        super(message);
    }
}
