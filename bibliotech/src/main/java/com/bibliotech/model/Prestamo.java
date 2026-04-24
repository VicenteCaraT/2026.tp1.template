package com.bibliotech.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Prestamo {
    private final int id;
    private final Socio socio;
    private final Recurso recurso;
    private final LocalDate fechaPrestamo;
    private final LocalDate fechaLimite;
    private LocalDate fechaDevolucion;
    private EstadoPrestamo estado;

    public Prestamo(int id, Socio socio, Recurso recurso, int diasPrestamo) {
        this.id = id;
        this.socio = socio;
        this.recurso = recurso;
        this.fechaPrestamo = LocalDate.now();
        this.fechaLimite = LocalDate.now().plusDays(diasPrestamo);
        this.estado = EstadoPrestamo.ACTIVO;
    }

    public void devolver() {
        this.fechaDevolucion = LocalDate.now();
        this.estado = EstadoPrestamo.DEVUELTO;
    }

    public void verificarVencimiento() {
        if (estado == EstadoPrestamo.ACTIVO && LocalDate.now().isAfter(fechaLimite)) {
            this.estado = EstadoPrestamo.VENCIDO;
        }
    }

    public int getId()                      { return id; }
    public Socio getSocio()                 { return socio; }
    public Recurso getRecurso()             { return recurso; }
    public LocalDate getFechaPrestamo()     { return fechaPrestamo; }
    public LocalDate getFechaLimite()       { return fechaLimite; }
    public LocalDate getFechaDevolucion()   { return fechaDevolucion; }
    public EstadoPrestamo getEstado()       { return estado; }

}
