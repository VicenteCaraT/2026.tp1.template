package com.bibliotech.repository;

import com.bibliotech.model.Socio;

import java.util.List;
import java.util.Optional;


public interface SocioRepository extends Repository<Socio, Integer> {
    Optional<Socio> buscarPorDni(String dni);
    Optional<Socio> buscarPorEmail(String email);
    List<Socio> buscarPorNombre(String nombre);
}
