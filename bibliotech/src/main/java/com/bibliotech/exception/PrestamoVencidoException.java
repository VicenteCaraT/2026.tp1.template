package com.bibliotech.exception;

public class PrestamoVencidoException extends PrestamoException {
    public PrestamoVencidoException() {
        super("Prestamo vencido");
    }
    public PrestamoVencidoException(String message) {
        super(message);
    }
}
