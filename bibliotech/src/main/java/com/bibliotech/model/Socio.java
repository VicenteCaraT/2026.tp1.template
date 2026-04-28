package com.bibliotech.model;

import java.time.LocalDate;

public abstract class Socio {
    private final int id;
    private final String nombre;
    private final String dni;
    private final String email;
    private boolean sancionado;
    private LocalDate fechaFinSancion;

    public Socio(int id, String nombre, String dni, String email) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.sancionado = false;
        this.fechaFinSancion = null;
    }

    public void sancionar(int dias) {
        this.sancionado = true;
        this.fechaFinSancion = LocalDate.now().plusDays(dias);
    }

    public void verificarSancion() {
        if (sancionado && LocalDate.now().isAfter(this.fechaFinSancion)) {
            this.sancionado = false;
            this.fechaFinSancion = null;
        }
    }

    public abstract int getLimitePrestamo();

    public boolean isSancionado() { return sancionado; }
    public LocalDate getFechaFinSancion() { return fechaFinSancion; }

    public int getId() {return id;}
    public String getNombre() {return nombre;}
    public String getDni() {return dni;}
    public String getEmail() {return email;}
}

