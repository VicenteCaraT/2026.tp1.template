package com.bibliotech.service;

import com.bibliotech.model.Socio;

import java.util.List;

public interface SancionService {
    void aplicarSancion(Integer socioId, int diasRetraso);
    void verificarSanciones();
    boolean isSancionado(Integer socioId);
    List<Socio> listarSancionados();
}
