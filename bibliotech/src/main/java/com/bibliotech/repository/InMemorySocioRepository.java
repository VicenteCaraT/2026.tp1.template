package com.bibliotech.repository;

import com.bibliotech.model.Socio;

import java.util.*;
import java.util.stream.Collectors;

public class InMemorySocioRepository implements SocioRepository {

    private final Map<Integer, Socio> socios = new HashMap<>();

    @Override
    public void guardar(Socio entidad) {
        socios.put(entidad.getId(), entidad);
    }

    @Override
    public Optional<Socio> buscarPorId(Integer id) {
        return Optional.ofNullable(socios.get(id));
    }

    @Override
    public List<Socio> buscarTodos() {
        return new ArrayList<>(socios.values());
    }

    @Override
    public Optional<Socio> buscarPorDni(String dni) {
        return  socios.values().stream()
                .filter(s -> s.getDni().equals(dni))
                .findFirst();
    }

    @Override
    public Optional<Socio> buscarPorEmail(String email) {
        return socios.values().stream()
                .filter(s -> s.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Socio> buscarPorNombre(String nombre) {
        return socios.values().stream()
                .filter(s -> s.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }
}
