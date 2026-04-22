package com.bibliotech.exception;

public class SocioSancionadoException extends SocioException {
    public SocioSancionadoException() {
        super("Socio se encuentra sancionado.");
    }
    public SocioSancionadoException(String message) {
        super(message);
    }
}
