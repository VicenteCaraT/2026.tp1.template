package com.bibliotech.repository;

import com.bibliotech.model.EstadoPrestamo;
import com.bibliotech.model.Prestamo;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryPrestamoRepository implements PrestamoRepository {
    private final Map<Integer, Prestamo> prestamos = new HashMap<>();

    @Override
    public void guardar(Prestamo entidad) {
        prestamos.put(entidad.getId(), entidad);
    }

    @Override
    public Optional<Prestamo> buscarPorId(Integer id) {
        return Optional.ofNullable(prestamos.get(id));
    }

    @Override
    public List<Prestamo> buscarTodos() {
        return new ArrayList<>(prestamos.values());
    }

    @Override
    public List<Prestamo> buscarPorSocio(Integer socioId) {
        return  prestamos.values().stream()
                .filter(p -> p.getSocio().getId() == socioId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Prestamo> buscarPorRecurso(String isbn) {
        return prestamos.values().stream()
                .filter(p -> p.getRecurso().isbn().equals(isbn))
                .collect(Collectors.toList());
    }

    @Override
    public List<Prestamo> buscarPorEstado(EstadoPrestamo estado) {
        return prestamos.values().stream()
                .filter(p -> p.getEstado().equals(estado))
                .collect(Collectors.toList());
    }
}
