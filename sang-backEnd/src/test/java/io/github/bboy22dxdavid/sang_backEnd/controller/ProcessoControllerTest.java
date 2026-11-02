package io.github.bboy22dxdavid.sang_backEnd.controller;


import io.github.bboy22dxdavid.sang_backEnd.model.Processo;
import io.github.bboy22dxdavid.sang_backEnd.repository.ProcessoRepository;
import io.github.bboy22dxdavid.sang_backEnd.service.ProcessoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // DIZ AO SPRING PARA USAR AS CONFIGURAÇÕES DO APPLICATION-TEST.YML
public class ProcessoControllerTest {


    @Autowired
    private ProcessoRepository repository;

    private ProcessoService service;

    @BeforeEach
    public void setUp() {
        // Inicialização manual da camada de serviço injetando o repositório real
        this.service = new ProcessoService(repository);
       // repository.deleteAll();
    }


    @Test
    public void deveSalvarProcessoManualmenteComSucesso() {
        // 1. Cenário
        Processo processoParaSalvar = new Processo(null, 2026);

        // 2. Ação
        Processo processoSalvo = service.salvar(processoParaSalvar);

        // 3. Validação manual via Asserts nativos do JUnit 5
        Assertions.assertNotNull(processoSalvo.getId(), "O ID deveria ter sido gerado pelo banco!");
        Assertions.assertEquals(2026, processoSalvo.getAno(), "O ano salvo deve ser exatamente 2026!");
    }



}
