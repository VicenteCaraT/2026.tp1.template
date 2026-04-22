package com.bibliotech.exception;

public class SocioNoEncontradoException extends SocioException {
    public SocioNoEncontradoException() {
        super("Socio no encontrado");
    }
    public SocioNoEncontradoException(String message) {
        super(message);
    }
}
