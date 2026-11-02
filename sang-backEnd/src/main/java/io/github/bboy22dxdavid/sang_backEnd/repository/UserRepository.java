package io.github.bboy22dxdavid.sang_backEnd.repository;


import io.github.bboy22dxdavid.sang_backEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Método indispensável para a futura tela de login do sistema
    Optional<User> findByUsername(String username);
}
