package com.bibliotech.service;

import com.bibliotech.model.Socio;

import java.util.List;
import java.util.Optional;

public interface SocioService {
    void registrarEstudiante(String nombre, String dni, String email);
    void registrarDocente(String nombre, String dni, String email);
    Optional<Socio> buscarPorId(Integer id);
    Optional<Socio> buscarPorDni(String dni);
    List<Socio> buscarTodos();
}
