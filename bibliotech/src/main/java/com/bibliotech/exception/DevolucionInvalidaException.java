package com.bibliotech.exception;

public class DevolucionInvalidaException extends PrestamoException {
    public DevolucionInvalidaException() {
        super("Devolucion invalida");
    }
    public DevolucionInvalidaException(String message) {
        super(message);
    }
}
