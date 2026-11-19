package io.github.bboy22dxdavid.sang_backEnd.repository;

import io.github.bboy22dxdavid.sang_backEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsernameAndAtivoTrue(String username);
}
