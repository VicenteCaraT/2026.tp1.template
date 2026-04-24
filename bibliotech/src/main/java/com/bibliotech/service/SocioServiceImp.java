package com.bibliotech.service;

import com.bibliotech.exception.DniDuplicadoException;
import com.bibliotech.exception.EmailDuplicadoException;
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
        if (socioRepository.buscarPorDni(socio.getDni()).isPresent()) {
            throw new DniDuplicadoException();
        }
        if (socioRepository.buscarPorEmail(socio.getEmail()).isPresent()) {
            throw new EmailDuplicadoException();
        }
        socioRepository.guardar(socio);
    }

    @Override
    public Optional<Socio> buscarPorId(Integer id) {
        return socioRepository.buscarPorId(id);
    }

    @Override
    public Optional<Socio> buscarPorDni(String dni) {
        return socioRepository.buscarPorDni(dni);
    }

    @Override
    public List<Socio> buscarTodos() {
        return socioRepository.buscarTodos();
    }
}
