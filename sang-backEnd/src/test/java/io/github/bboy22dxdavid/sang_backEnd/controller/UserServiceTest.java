package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.dto.UserDTO;
import io.github.bboy22dxdavid.sang_backEnd.model.User;
import io.github.bboy22dxdavid.sang_backEnd.service.UserService;
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

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Deve cadastrar um usuário válido e gerar um identificador UUID nativo")
    public void deveCadastrarUsuarioComUUID() {
        System.out.println("\n==============================================");
        System.out.println(">>> INICIANDO TESTE: CADASTRO COM UUID NATIVO <<<");
        System.out.println("==============================================");

        // 1. Instancia um usuário de teste
        String usernameUnico = "avaliador_" + System.currentTimeMillis();
        User novoUsuario = new User(usernameUnico, "senhaSegura123", 1); // 1 = ROLE_AVALIADOR

        // 2. Executa a ação de cadastro através do Service
        UserDTO resultado = userService.cadastrar(novoUsuario);

        // 3. Validações Críticas (Padrão correto JUnit 5: esperado, atual, mensagem)
        assertNotNull(resultado, "O objeto retornado não deveria ser nulo.");
        assertNotNull(resultado.getId(), "O ID no formato UUID deveria ter sido gerado.");
        assertEquals(usernameUnico, resultado.getUsername(), "O username retornado deve ser igual ao enviado.");
        assertEquals(1, resultado.getRoleId(), "O papel de acesso (RoleId) deve persistir corretamente.");
        assertInstanceOf(UUID.class, resultado.getId(), "O ID precisa ser obrigatoriamente um objeto do tipo UUID.");

        // Imprime os resultados formatados de forma clara no terminal
        System.out.println("✅ Usuário cadastrado com sucesso no ambiente integrado!");
        System.out.println("ID Gerado (UUID): " + resultado.getId());
        System.out.println("Username Salvo:   " + resultado.getUsername());
        System.out.println("RoleId Salvo:     " + resultado.getRoleId());
        System.out.println("==============================================");
        System.out.println(">>> TESTE CONCLUÍDO COM SUCESSO! <<<");
        System.out.println("==============================================\n");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar um username duplicado")
    public void naoDevePermitirUsernameDuplicado() {
        String usernameRepetido = "usuario_duplicado";
        User user1 = new User(usernameRepetido, "senha1", 1);
        User user2 = new User(usernameRepetido, "senha2", 2);

        // Salva o primeiro sem problemas
        userService.cadastrar(user1);

        // Força a validação de erro ao tentar inserir o segundo com o mesmo username
        assertThrows(RuntimeException.class, () -> {
            userService.cadastrar(user2);
        }, "Deveria ter lançado uma RuntimeException devido ao username duplicado.");

        System.out.println("✅ Bloqueio de username duplicado validado com sucesso!");
    }

}
