package com.bibliotech.service;

import com.bibliotech.model.Socio;

import java.util.List;
import java.util.Optional;

public interface SocioService {
    void registrarSocio(Socio socio);
    Optional<Socio> buscarPorId(Integer id);
    Optional<Socio> buscarPorDni(String dni);
    List<Socio> buscarTodos();
}
