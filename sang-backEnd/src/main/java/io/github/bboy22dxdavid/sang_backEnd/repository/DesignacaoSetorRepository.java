package io.github.bboy22dxdavid.sang_backEnd.repository;

import io.github.bboy22dxdavid.sang_backEnd.model.DesignacaoSetor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DesignacaoSetorRepository extends JpaRepository<DesignacaoSetor, UUID> {
    List<DesignacaoSetor> findByProcessoId(UUID processoId);
    List<DesignacaoSetor> findByProcessoIdAndAvaliadorId(UUID processoId, UUID avaliadorId);
    Optional<DesignacaoSetor> findByProcessoIdAndSecao(UUID processoId, String secao);
}
