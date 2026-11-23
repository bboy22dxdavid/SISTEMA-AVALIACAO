package io.github.bboy22dxdavid.sang_backEnd.repository;

import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {
    boolean existsByProcessoIdAndAvaliadoId(UUID processoId, UUID avaliadoId);
    List<Avaliacao> findByProcessoId(UUID processoId);
}
