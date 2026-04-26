package com.bibliotech.service;

import com.bibliotech.exception.DniDuplicadoException;
import com.bibliotech.exception.EmailDuplicadoException;
import com.bibliotech.model.Docente;
import com.bibliotech.model.Estudiante;
import com.bibliotech.model.Socio;
import com.bibliotech.repository.SocioRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class SocioServiceImp implements SocioService {

    private final SocioRepository socioRepository;
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    // DIP
    public SocioServiceImp(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }


    @Override
    public void registrarEstudiante(String nombre, String dni, String email) {
        validarYRegistrar(nombre, dni, email, "ESTUDIANTE");
    }

    @Override
    public void registrarDocente(String nombre, String dni, String email) {
        validarYRegistrar(nombre, dni, email, "DOCENTE");
    }

    private void validarYRegistrar(String nombre, String dni, String email, String tipo) {
        Validador.validarNombre(nombre);
        Validador.validarDni(dni);
        Validador.validarEmail(email);

        if (socioRepository.buscarPorDni(dni).isPresent()) {
            throw new DniDuplicadoException();
        }
        if (socioRepository.buscarPorEmail(email).isPresent()) {
            throw new EmailDuplicadoException();
        }

        int id = idGenerator.getAndIncrement();
        Socio socio = tipo.equals("ESTUDIANTE")
                ? new Estudiante(id, nombre, dni, email)
                : new Docente(id, nombre, dni, email);

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
