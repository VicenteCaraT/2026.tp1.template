package com.bibliotech.exception;

public class DniDuplicadoException extends ValidacionException {
    public DniDuplicadoException() {
        super("Dni duplicado");
    }
    public DniDuplicadoException(String message) {
        super(message);
    }
}
