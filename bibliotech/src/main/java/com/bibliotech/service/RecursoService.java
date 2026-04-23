package com.bibliotech.service;

import com.bibliotech.model.Categoria;
import com.bibliotech.model.Recurso;

import java.util.List;
import java.util.Optional;

public interface RecursoService {
    void registrarRecurso(Recurso recurso);
    Optional<Recurso> buscarPorIsbn(String isbn);
    List<Recurso> buscarTodos();
    List<Recurso> buscarPorTitulo(String titulo);
    List<Recurso> buscarPorAutor(String autor);
    List<Recurso> buscarPorCategoria(Categoria categoria);
}
