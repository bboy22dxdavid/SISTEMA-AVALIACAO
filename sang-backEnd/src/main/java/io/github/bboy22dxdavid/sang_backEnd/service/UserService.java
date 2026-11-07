package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.dto.UserDTO;
import io.github.bboy22dxdavid.sang_backEnd.model.Role;
import io.github.bboy22dxdavid.sang_backEnd.model.User;
import io.github.bboy22dxdavid.sang_backEnd.repository.RoleRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDTO cadastrarUsuario(User user, boolean autorizarGraduadoAvaliador) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username já cadastrado no sistema.");
        }
        if (userRepository.findBySaram(user.getSaram()).isPresent()) {
            throw new IllegalArgumentException("SARAM já cadastrado na base de usuários.");
        }

        if (user.getCodposto() == null || user.getCodposto().trim().isEmpty()) {
            throw new IllegalArgumentException("O código do posto é obrigatório.");
        }

        String codigoPosto = user.getCodposto().toUpperCase().trim();
        String nomeRole = definirRolePorPosto(codigoPosto, autorizarGraduadoAvaliador);

        Role roleAtribuida = roleRepository.findByRole(nomeRole)
                .orElseThrow(() -> new RuntimeException("Perfil de acesso não encontrado: " + nomeRole));

        user.setRole(roleAtribuida);
        if (user.getAtivo() == null) {
            user.setAtivo(true);
        }

        User usuarioSalvo = userRepository.save(user);
        return converterParaDTO(usuarioSalvo);
    }

    private String definirRolePorPosto(String codigoPosto, boolean autorizarGraduadoAvaliador) {
        switch (codigoPosto) {
            case "SO":
            case "1SGT":
            case "2SGT":
            case "3SGT":
                if (!autorizarGraduadoAvaliador) {
                    throw new IllegalArgumentException("Graduados exigem autorização de exceção para atuar como avaliadores.");
                }
                return "ROLE_AVALIADOR";
            case "2TEN":
            case "1TEN":
            case "CAP":
                return "ROLE_AVALIADOR";
            case "MAJ":
            case "TCEL":
            case "CEL":
                return "ROLE_REVISOR";
            default:
                throw new IllegalArgumentException("Código de posto não reconhecido: " + codigoPosto);
        }
    }

    public List<UserDTO> listarTodos() {
        return userRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public UserDTO buscarPorId(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
        return converterParaDTO(user);
    }

    public void deletar(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
        userRepository.delete(user);
    }

    // Método auxiliar centralizado para evitar repetição de código e erros de tipagem no DTO
    private UserDTO converterParaDTO(User u) {
        UUID roleId = (u.getRole() != null) ? u.getRole().getId() : null;
        return new UserDTO(u.getId(), roleId, u.getUsername());
    }
}
