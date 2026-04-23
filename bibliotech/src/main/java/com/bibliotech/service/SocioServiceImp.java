package com.bibliotech.service;

import com.bibliotech.model.Socio;
import com.bibliotech.repository.SocioRepository;

import java.util.List;
import java.util.Optional;

public class SocioServiceImp implements SocioService {

    private final SocioRepository socioRepository;

    // DIP
    public SocioServiceImp(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }


    @Override
    public void registrarSocio(Socio socio) {
        // TODO: implement
    }

    @Override
    public Optional<Socio> buscarPorId(Integer id) {
        return socioRepository.buscarPorId(id);
    }

    @Override
    public Optional<Socio> buscarPorDni(String dni) {
        // TODO: implement
        return Optional.empty();
    }

    @Override
    public List<Socio> buscarTodos() {
        // TODO: implement
        return List.of();
    }
}
