package io.github.bboy22dxdavid.sang_backEnd.repository;

import io.github.bboy22dxdavid.sang_backEnd.model.Efetivo;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EfetivoProviderPort {
    List<Efetivo> buscarElegiveis();
    List<Efetivo> buscarElegiveisPorSecao(String secao);
    Optional<Efetivo> buscarPorId(UUID id);
    Optional<Efetivo> buscarPorSaram(String saram);
}
