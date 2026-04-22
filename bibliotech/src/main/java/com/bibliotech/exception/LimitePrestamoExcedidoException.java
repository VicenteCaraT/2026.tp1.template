package com.bibliotech.exception;

public class LimitePrestamoExcedidoException extends PrestamoException {
    public LimitePrestamoExcedidoException() {
        super("Limite de prestamos excedido");
    }
    public LimitePrestamoExcedidoException(String message) {
        super(message);
    }
}
