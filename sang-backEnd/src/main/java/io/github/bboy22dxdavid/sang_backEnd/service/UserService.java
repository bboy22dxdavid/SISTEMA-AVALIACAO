package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.dto.UserRequest;
import io.github.bboy22dxdavid.sang_backEnd.model.Role;
import io.github.bboy22dxdavid.sang_backEnd.model.User;
import io.github.bboy22dxdavid.sang_backEnd.repository.RoleRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> listarTodos() {
        return userRepository.findAll();
    }

    public User buscarPorId(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    }

    @Transactional
    public User criar(UserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com o login: " + request.username());
        }
        if (userRepository.existsBySaram(request.saram())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com o SARAM: " + request.saram());
        }

        validarRegraCoronelRevisor(request.codPosto(), request.roleName());

        Role role = roleRepository.findByName(request.roleName())
                .orElseThrow(() -> new EntityNotFoundException("Perfil de acesso inválido: " + request.roleName()));

        User user = new User();
        user.setUsername(request.username().trim().toLowerCase());
        user.setSaram(request.saram().trim());
        user.setPosto(request.posto().trim());
        user.setCodPosto(request.codPosto().trim().toUpperCase());
        user.setAntiguidade(request.antiguidade());
        user.setRole(role);
        user.setAtivo(request.ativo() != null ? request.ativo() : true);

        return userRepository.save(user);
    }

    @Transactional
    public User atualizar(UUID id, UserRequest request) {
        User user = buscarPorId(id);

        validarRegraCoronelRevisor(request.codPosto(), request.roleName());

        Role role = roleRepository.findByName(request.roleName())
                .orElseThrow(() -> new EntityNotFoundException("Perfil de acesso inválido: " + request.roleName()));

        user.setPosto(request.posto().trim());
        user.setCodPosto(request.codPosto().trim().toUpperCase());
        user.setAntiguidade(request.antiguidade());
        user.setRole(role);
        if (request.ativo() != null) {
            user.setAtivo(request.ativo());
        }

        return userRepository.save(user);
    }

    private void validarRegraCoronelRevisor(String codPosto, String roleName) {
        if ("ROLE_REVIEWER".equalsIgnoreCase(roleName) && !"CEL".equalsIgnoreCase(codPosto)) {
            throw new IllegalArgumentException("Regra de Negócio: Apenas militares no posto de Coronel (CEL) podem receber o perfil ROLE_REVIEWER.");
        }
    }
}
