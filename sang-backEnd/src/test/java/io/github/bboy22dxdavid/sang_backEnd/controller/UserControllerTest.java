package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.model.User;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc // Configura o MockMvc para simular requisições HTTP
public class UserControllerTest {

    //@Autowired
    private MockMvc mockMvc;




    @Test
    @DisplayName("Deve gravar o usuário fisicamente no banco através do endpoint HTTP POST")
    public void deveGravarUsuarioViaPost() throws Exception {
        System.out.println("\n==============================================");
        System.out.println(">>> INICIANDO TESTE: REQUISIÇÃO HTTP POST   <<<");
        System.out.println("==============================================");

        // 1. Define um username dinâmico para evitar duplicidade
        String usernameFisico = "militar_persistido_" + System.currentTimeMillis();

        // 2. Monta o JSON manualmente de forma limpa usando Text Blocks do Java
        String usuarioJson = """
            {
                "username": "%s",
                "password": "senhaPost123",
                "roleId": 2
            }
            """.formatted(usernameFisico);

        // 3. Dispara a requisição HTTP POST simulando o Front-end
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson)) // Passa a string JSON direta aqui

                // 4. Validações do protocolo HTTP
                .andExpect(status().isCreated()) // Espera HTTP Status 201 (Created)
                .andExpect(jsonPath("$.id").exists()) // Garante que o JSON de retorno tem o campo ID (UUID)
                .andExpect(jsonPath("$.username").value(usernameFisico))
                .andExpect(jsonPath("$.roleId").value(2));

        System.out.println("✅ Requisição HTTP POST realizada com sucesso!");
        System.out.println("✅ Usuário '" + usernameFisico + "' persistido permanentemente no banco.");
        System.out.println("==============================================");
        System.out.println(">>> TESTE CONCLUÍDO COM SUCESSO! <<<");
        System.out.println("==============================================\n");
    }

}
