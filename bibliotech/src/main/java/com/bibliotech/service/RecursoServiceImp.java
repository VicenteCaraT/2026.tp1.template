package com.bibliotech.service;

import com.bibliotech.exception.IsbnDuplicadoException;
import com.bibliotech.model.Categoria;
import com.bibliotech.model.Recurso;
import com.bibliotech.repository.RecursoRepository;

import java.util.List;
import java.util.Optional;

public class RecursoServiceImp implements RecursoService {

    private final RecursoRepository recursoRepository;

    // DIP
    public RecursoServiceImp(RecursoRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }

    @Override
    public  void registrarRecurso(Recurso recurso) {
        Validador.validarIsbn(recurso.isbn());

        if (recursoRepository.buscarPorId(recurso.isbn()).isPresent()) {
            throw new IsbnDuplicadoException();
        }
        recursoRepository.guardar(recurso);
    }

    @Override
    public Optional<Recurso> buscarPorIsbn(String isbn) {
        return recursoRepository.buscarPorId(isbn);
    }

    @Override
    public List<Recurso> buscarTodos() {
        return recursoRepository.buscarTodos();
    }

    @Override
    public List<Recurso> buscarPorTitulo(String titulo) {
        return recursoRepository.buscarPorTitulo(titulo);
    }

    @Override
    public List<Recurso> buscarPorAutor(String autor) {
        return recursoRepository.buscarPorAutor(autor);
    }

    @Override
    public List<Recurso> buscarPorCategoria(Categoria categoria) {
        return recursoRepository.buscarPorCategoria(categoria);
    }
}
