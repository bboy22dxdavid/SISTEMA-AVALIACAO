package io.github.bboy22dxdavid.sang_backEnd.repository;

import io.github.bboy22dxdavid.sang_backEnd.model.Nivelamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NivelamentoRepository extends JpaRepository<Nivelamento, Long> {
    // Método para buscar o nivelamento a partir da avaliação de origem (relação 1:1)
    Optional<Nivelamento> findByAvaliacaoId(Long avaliacaoId);
}
