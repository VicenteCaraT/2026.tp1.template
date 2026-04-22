package com.bibliotech.exception;

public class SocioDuplicadoException extends SocioException {
    public SocioDuplicadoException(){
        super("Socio duplicado");
    }
    public SocioDuplicadoException(String message) {
        super(message);
    }
}
