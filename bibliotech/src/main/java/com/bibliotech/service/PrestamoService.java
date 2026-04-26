package com.bibliotech.service;

import com.bibliotech.model.Prestamo;

import java.util.List;
import java.util.Optional;

public interface PrestamoService {
    void realizarPrestamo(Integer socioId, String isbn, int diasPrestamo);
    void devolverPrestamo(Integer prestamoId);
    void verificarVencimientos();
    Optional<Prestamo> buscarPorId(Integer id);
    List<Prestamo> buscarPorSocioId(Integer socioId);
    List<Prestamo> buscarPorRecurso(String isbn);
    List<Prestamo> buscarTodos();

}
