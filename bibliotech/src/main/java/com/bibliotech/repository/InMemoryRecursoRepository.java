package com.bibliotech.repository;

import com.bibliotech.model.Recurso;

import java.util.*;

public class InMemoryRecursoRepository implements RecursoRepository {

    private final Map<String, Recurso> recursos = new HashMap<>();

    @Override
    public void guardar(Recurso entidad) {
        recursos.put((entidad.isbn()), entidad);
    }

    @Override
    public Optional<Recurso> BuscarPorId(String isbn) {
        return Optional.ofNullable(recursos.get(isbn));
    }

    @Override
    public List<Recurso> buscarTodos() {
        return new ArrayList<>(recursos.values());
    }
}
