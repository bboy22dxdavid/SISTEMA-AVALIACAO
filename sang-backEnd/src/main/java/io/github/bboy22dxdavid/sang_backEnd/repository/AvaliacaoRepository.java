package io.github.bboy22dxdavid.sang_backEnd.repository;


import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long>  {

    // Método útil para listar o ranking do processo anual ordenado
    List<Avaliacao> findByProcessoId(Long processoId);
}
