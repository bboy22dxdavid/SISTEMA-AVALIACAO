package io.github.bboy22dxdavid.sang_backEnd.repository;


import io.github.bboy22dxdavid.sang_backEnd.model.Avaliado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvaliadoRepository extends JpaRepository<Avaliado, Long>{

    // Método útil para buscar militares pelo SARAM no botão de pesquisa
    Optional<Avaliado> findBySaram(String saram);
}
