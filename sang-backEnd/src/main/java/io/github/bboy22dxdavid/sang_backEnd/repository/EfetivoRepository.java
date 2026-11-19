package io.github.bboy22dxdavid.sang_backEnd.repository;

import io.github.bboy22dxdavid.sang_backEnd.model.Efetivo;
import io.github.bboy22dxdavid.sang_backEnd.model.TipoVinculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EfetivoRepository extends JpaRepository<Efetivo, UUID> {
    List<Efetivo> findByTipoVinculoAndAtivoTrueOrderByAntiguidadeAsc(TipoVinculo tipoVinculo);
    List<Efetivo> findBySecaoAndTipoVinculoAndAtivoTrueOrderByAntiguidadeAsc(String secao, TipoVinculo tipoVinculo);
    Optional<Efetivo> findBySaram(String saram);
}