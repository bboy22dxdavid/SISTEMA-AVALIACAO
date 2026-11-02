package io.github.bboy22dxdavid.sang_backEnd.repository;

import io.github.bboy22dxdavid.sang_backEnd.model.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/* =========================================================
 * Interface ProcessoRepository que estende o JpaRepository
 * herda todos os métodos de CRUD
 * =========================================================
 * */
@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Long> {
}
