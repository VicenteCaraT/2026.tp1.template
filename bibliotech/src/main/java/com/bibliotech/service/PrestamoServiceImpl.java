package com.bibliotech.service;

import com.bibliotech.exception.*;
import com.bibliotech.model.*;
import com.bibliotech.repository.PrestamoRepository;
import com.bibliotech.repository.RecursoRepository;
import com.bibliotech.repository.SocioRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PrestamoServiceImpl implements PrestamoService {
    private final PrestamoRepository prestamoRepository;
    private final RecursoRepository recursoRepository;
    private final SocioRepository socioRepository;
    private final SancionService sancionService;
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public PrestamoServiceImpl(
            PrestamoRepository prestamoRepository,
            RecursoRepository recursoRepository,
            SocioRepository socioRepository,
            SancionService sancionService) {
        this.prestamoRepository = prestamoRepository;
        this.recursoRepository = recursoRepository;
        this.socioRepository = socioRepository;
        this.sancionService = sancionService;
    }

    @Override
    public void realizarPrestamo(Integer socioId, String isbn, int diasPrestamo) {
        Socio socio = socioRepository.buscarPorId(socioId)
                .orElseThrow(SocioNoEncontradoException::new);

        // Verificar sanción
        sancionService.verificarSanciones();
        if (socio.isSancionado()) {
            throw new SocioSancionadoException("El socio " + socio.getNombre() + " está sancinado hasta " + socio.getFechaFinSancion());
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

        if (prestamo.getEstado() == EstadoPrestamo.VENCIDO) {
            long diasRetraso = ChronoUnit.DAYS.between(
                    prestamo.getFechaLimite(), LocalDate.now());
            sancionService.aplicarSancion(prestamo.getSocio().getId(), (int)  diasRetraso);
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
    public List<Prestamo> listarActivos() {
        return prestamoRepository.buscarPorEstado(EstadoPrestamo.ACTIVO);
    }

    @Override
    public List<Prestamo> listarVencidos() {
        return prestamoRepository.buscarPorEstado(EstadoPrestamo.VENCIDO);
    }

    @Override
    public List<Prestamo> proximosAVencer(int dias) {
        LocalDate limite = LocalDate.now().plusDays(dias);
        return listarActivos().stream()
                .filter(p -> !p.getFechaLimite().isAfter(limite))
                .collect(Collectors.toList());
    }

    @Override
    public boolean verificarDisponibilidad(String isbn) {
        return buscarPorRecurso(isbn).stream()
                .noneMatch(p -> p.getEstado() == EstadoPrestamo.ACTIVO);
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
