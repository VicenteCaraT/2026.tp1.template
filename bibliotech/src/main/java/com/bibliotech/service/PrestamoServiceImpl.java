package com.bibliotech.service;

import com.bibliotech.exception.*;
import com.bibliotech.model.*;
import com.bibliotech.repository.PrestamoRepository;
import com.bibliotech.repository.RecursoRepository;
import com.bibliotech.repository.SocioRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class PrestamoServiceImpl implements PrestamoService {
    private final PrestamoRepository prestamoRepository;
    private final RecursoRepository recursoRepository;
    private final SocioRepository socioRepository;
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public PrestamoServiceImpl(
            PrestamoRepository prestamoRepository,
            RecursoRepository recursoRepository,
            SocioRepository socioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.recursoRepository = recursoRepository;
        this.socioRepository = socioRepository;
    }

    @Override
    public void realizarPrestamo(Integer socioId, String isbn, int diasPrestamo) {
        Socio socio = socioRepository.buscarPorId(socioId)
                .orElseThrow(SocioNoEncontradoException::new);

        // Verificar sanción
        if (socio instanceof Estudiante || socio instanceof Docente) {
            // lógica sanción
        }

        // Verificar límite de prestamo
        long prestamosActivos = prestamoRepository.buscarPorSocio(socioId)
                .stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.ACTIVO)
                .count();
        if (prestamosActivos >= socio.getLimitePrestamo()) {
            throw new LimitePrestamoExcedidoException();
        }

        // Verificar existencia de recurso
        Recurso recurso = recursoRepository.buscarPorId(isbn)
                .orElseThrow(RecursoNoEncontradoException::new);

        // Verificar que el recurso no tenga prestamo activo
        boolean tienePrestamoActivo = prestamoRepository.buscarPorRecurso(isbn)
                .stream()
                .anyMatch(p -> p.getEstado() == EstadoPrestamo.ACTIVO);

        if (tienePrestamoActivo) {
            throw new RecursoConPrestamoException();
        }

        // Crear prestamo
        Prestamo prestamo = new Prestamo(idGenerator.getAndIncrement(), socio, recurso, diasPrestamo);
        prestamoRepository.guardar(prestamo);
    }

    @Override
    public void devolverPrestamo(Integer prestamoId) {
        // Verificar existencia de prestamo
        Prestamo prestamo = prestamoRepository.buscarPorId(prestamoId)
                .orElseThrow(PrestamoNoEncontradoException::new);
        // Verificar si ya fue devuelto
        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new DevolucionInvalidaException();
        }
        // Devolver
        prestamo.devolver();
        prestamoRepository.guardar(prestamo);
    }

    @Override
    public void verificarVencimientos() {
        prestamoRepository.buscarPorEstado(EstadoPrestamo.ACTIVO)
                .forEach(Prestamo::verificarVencimiento);

    }

    @Override
    public Optional<Prestamo> buscarPorId(Integer id) {
        return prestamoRepository.buscarPorId(id);
    }

    @Override
    public List<Prestamo> buscarPorSocioId(Integer socioId) {
        return prestamoRepository.buscarPorSocio(socioId);
    }

    @Override
    public List<Prestamo> buscarPorRecurso(String isbn) {
        return prestamoRepository.buscarPorRecurso(isbn);
    }

    @Override
    public List<Prestamo> buscarTodos() {
        return prestamoRepository.buscarTodos();
    }
}
