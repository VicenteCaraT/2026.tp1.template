package com.bibliotech.exception;

public class IsbnDuplicadoException extends ValidacionException {
    public IsbnDuplicadoException() {
        super("ISBN duplicado");
    }
    public IsbnDuplicadoException(String message) {
        super(message);
    }
}
