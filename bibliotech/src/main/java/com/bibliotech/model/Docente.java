package com.bibliotech.model;

public class Docente extends Socio {
    public Docente(int id, String nombre, String dni, String email) {
        super(id, nombre, dni, email);
    }

    @Override
    public int getLimitePrestamo() { return 5; }
}
