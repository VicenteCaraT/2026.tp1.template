package com.bibliotech.exception;

public class RecursoNoEncontradoException extends RecursoException {
    public RecursoNoEncontradoException() {
        super("Recurso no encontrado");
    }
    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}
