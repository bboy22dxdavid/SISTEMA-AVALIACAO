package io.github.bboy22dxdavid.sang_backEnd.repository;

import io.github.bboy22dxdavid.sang_backEnd.model.Nivelamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NivelamentoRepository extends JpaRepository<Nivelamento, UUID> {
    boolean existsByAvaliacaoId(UUID avaliacaoId);
    Optional<Nivelamento> findByAvaliacaoId(UUID avaliacaoId);
    List<Nivelamento> findByAvaliacaoProcessoId(UUID processoId);
}
