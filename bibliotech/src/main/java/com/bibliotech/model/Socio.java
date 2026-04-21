package com.bibliotech.model;

public abstract class Socio {
    private final int id;
    private final String nombre;
    private final String dni;
    private final String email;

    public Socio(int id, String nombre, String dni, String email) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
    }

    public abstract int getLimitePrestamo();

    public int getId() {return id;}
    public String getNombre() {return nombre;}
    public String getDni() {return dni;}
    public String getEmail() {return email;}
}

