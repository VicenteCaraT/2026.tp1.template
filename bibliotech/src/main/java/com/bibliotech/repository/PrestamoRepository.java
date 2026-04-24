package com.bibliotech.repository;

import com.bibliotech.model.EstadoPrestamo;
import com.bibliotech.model.Prestamo;

import java.util.List;

public interface PrestamoRepository extends Repository<Prestamo, Integer> {
    List<Prestamo> buscarPorSocio(Integer socioId);
    List<Prestamo> buscarPorRecurso(String isbn);
    List<Prestamo> buscarPorEstado(EstadoPrestamo estado);

}
