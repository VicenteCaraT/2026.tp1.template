package com.bibliotech.service;

import com.bibliotech.exception.SocioNoEncontradoException;
import com.bibliotech.model.Socio;
import com.bibliotech.repository.SocioRepository;

import java.util.List;
import java.util.stream.Collectors;

public class SancionServiceImp implements SancionService {

    private final SocioRepository socioRepository;
    private static final int DIAS_SANCION_POR_DIA_RETRASO = 2;

    public SancionServiceImp(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Override
    public void aplicarSancion(Integer socioId, int diasRetraso) {
        Socio socio = socioRepository.buscarPorId(socioId)
                .orElseThrow(SocioNoEncontradoException::new);
        int diasSancion = diasRetraso * DIAS_SANCION_POR_DIA_RETRASO;
        socio.sancionar(diasSancion);
        socioRepository.guardar(socio);
    }

    @Override
    public void verificarSanciones() {
        socioRepository.buscarTodos()
                .stream()
                .filter(Socio::isSancionado)
                .forEach(s -> {
                    s.verificarSancion();
                    socioRepository.guardar(s);
                });
    }

    @Override
    public boolean isSancionado(Integer socioId) {
        return socioRepository.buscarPorId(socioId)
                .map(Socio::isSancionado)
                .orElseThrow(SocioNoEncontradoException::new);
    }

    @Override
    public List<Socio> listarSancionados() {
        return  socioRepository.buscarTodos().stream()
                .filter(Socio::isSancionado)
                .collect(Collectors.toList());
    }
}
