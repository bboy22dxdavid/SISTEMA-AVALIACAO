package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.dto.UserDTO;
import io.github.bboy22dxdavid.sang_backEnd.model.Role;
import io.github.bboy22dxdavid.sang_backEnd.model.User;
import io.github.bboy22dxdavid.sang_backEnd.repository.RoleRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.UserRepository;
import io.github.bboy22dxdavid.sang_backEnd.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional // Garante que o banco dê ROLLBACK após o teste, mantendo-o limpo
public class UserServiceTest {

    //@Autowired
    private UserService userService;

    //@Autowired
    private UserRepository userRepository;

   // @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void prepararAmbiente() {
        // Garante que o perfil ROLE_AVALIADOR exista na tb_roles antes do teste
        if (roleRepository.findByRole("ROLE_AVALIADOR").isEmpty()) {
            Role roleAvaliador = new Role();
            roleAvaliador.setRole("ROLE_AVALIADOR");
            roleRepository.save(roleAvaliador);
        }

        // Remove o usuário de teste caso já exista de uma execução anterior
        userRepository.findByUsername("ten.silva.teste").ifPresent(userRepository::delete);
    }

    @Test
    public void deveConectarNoBancoESalvarUmUsuarioManualmente() {
        // 1. Monta o objeto User manualmente (sem CamelCase nos atributos)
        User novoUsuario = new User();
        novoUsuario.setUsername("ten.silva.teste");
        novoUsuario.setPassword("senha123");
        novoUsuario.setSaram("6543210");
        novoUsuario.setNguerra("Ten. Silva");
        novoUsuario.setSecao("GDA");
        novoUsuario.setCodposto("1TEN");
        novoUsuario.setPosto("Primeiro Tenente");
        novoUsuario.setQuadro("QOAV");
        novoUsuario.setFuncao("Piloto");
        novoUsuario.setAntiguidade(12);
        novoUsuario.setAtivo(true);

        // 2. Chama o serviço para aplicar a regra de posto e salvar no PostgreSQL
        UserDTO usuarioSalvo = userService.cadastrarUsuario(novoUsuario, false);

        // 3. Validações de conexão, geração de UUID e persistência
        Assertions.assertNotNull(usuarioSalvo.getId(), "O UUID do usuário deveria ter sido gerado pelo banco.");
        Assertions.assertNotNull(usuarioSalvo.getRoleId(), "O UUID da Role deveria ter sido vinculado.");
        Assertions.assertEquals("ten.silva.teste", usuarioSalvo.getUsername());

        System.out.println("✅ SUCESSO! Usuário salvo no PostgreSQL com UUID: " + usuarioSalvo.getId());
    }

    @Test
    public void deveListarUsuariosDoBancoComSucesso() {
        // 1. Garante que exista ao menos um registro no banco para ser listado
        if (userRepository.findByUsername("ten.silva.teste").isEmpty()) {
            deveConectarNoBancoESalvarUmUsuarioManualmente();
        }

        // 2. Chama o método listarTodos() do UserService
        java.util.List<UserDTO> usuarios = userService.listarTodos();

        // 3. Validações de retorno
        Assertions.assertNotNull(usuarios, "A lista de usuários não deveria ser nula.");
        Assertions.assertFalse(usuarios.isEmpty(), "A lista deveria conter pelo menos um usuário cadastrado.");

        // 4. Exibe os registros encontrados no console do IntelliJ
        System.out.println("=======================================================");
        System.out.println("📋 USUÁRIOS ENCONTRADOS NO BANCO (" + usuarios.size() + "):");
        for (UserDTO dto : usuarios) {
            System.out.println("   -> ID (UUID): " + dto.getId() +
                    " | Username: " + dto.getUsername() +
                    " | Role ID: " + dto.getRoleId());
        }
        System.out.println("=======================================================");
    }

}
