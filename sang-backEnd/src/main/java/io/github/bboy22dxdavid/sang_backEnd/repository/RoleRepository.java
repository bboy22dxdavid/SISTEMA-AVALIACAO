package io.github.bboy22dxdavid.sang_backEnd.repository;


import io.github.bboy22dxdavid.sang_backEnd.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Método customizado útil para buscar perfis pelo nome (ex: "ADMIN", "AVALIADOR")
    Optional<Role> findByRole(String role);
}
