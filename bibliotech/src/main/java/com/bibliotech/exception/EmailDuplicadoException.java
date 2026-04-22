package com.bibliotech.exception;

public class EmailDuplicadoException extends ValidacionException {
    public EmailDuplicadoException() {
        super("Email duplicado");
    }
    public EmailDuplicadoException(String message) {
        super(message);
    }
}
