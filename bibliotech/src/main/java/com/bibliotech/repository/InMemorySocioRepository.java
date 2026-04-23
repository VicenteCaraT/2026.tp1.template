package com.bibliotech.repository;

import com.bibliotech.model.Socio;

import java.util.*;

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
}
