package com.bibliotech.repository;

import com.bibliotech.model.Categoria;
import com.bibliotech.model.Recurso;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRecursoRepository implements RecursoRepository {

    private final Map<String, Recurso> recursos = new HashMap<>();

    @Override
    public void guardar(Recurso entidad) {
        recursos.put((entidad.isbn()), entidad);
    }

    @Override
    public Optional<Recurso> buscarPorId(String isbn) {
        return Optional.ofNullable(recursos.get(isbn));
    }

    @Override
    public List<Recurso> buscarTodos() {
        return new ArrayList<>(recursos.values());
    }

    @Override
    public List<Recurso> buscarPorTitulo(String titulo) {
        return recursos.values().stream()
                .filter(r -> r.titulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Recurso> buscarPorAutor(String autor) {
        return recursos.values().stream()
                .filter(r -> r.autor().toLowerCase().contains(autor.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Recurso> buscarPorCategoria(Categoria categoria) {
        return recursos.values().stream()
                .filter(r -> r.categoria().equals(categoria))
                .collect(Collectors.toList());
    }


}
