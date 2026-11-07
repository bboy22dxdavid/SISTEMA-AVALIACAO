package io.github.bboy22dxdavid.sang_backEnd.repository;


import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, UUID>  {

    // Método útil para listar o ranking do processo anual ordenado
    List<Avaliacao> findByProcessoId(UUID processoId);
    boolean existsByAvaliadoIdAndProcessoId(UUID avaliadoId, UUID processoId);
}
