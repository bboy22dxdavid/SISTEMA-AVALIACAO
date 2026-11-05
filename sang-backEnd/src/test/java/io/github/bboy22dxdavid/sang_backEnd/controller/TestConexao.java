package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import io.github.bboy22dxdavid.sang_backEnd.service.AvaliacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootTest
public class TestConexao {
    // TESTE MANUAL CONTROLADO DIRETO NO CONSOLE
    @Bean
    @Test
    public CommandLineRunner testarConexaoEBanco(AvaliacaoService avaliacaoService) {
        return args -> {
            System.out.println("\n=======================================================");
            System.out.println("🚀 [SANG] INICIANDO TESTE INTERNO DE LEITURA DO BANCO...");
            System.out.println("=======================================================");

            try {
                // Tenta buscar os registros que populamos via pgAdmin diretamente pelas camadas do Spring
                List<Avaliacao> avaliacoes = avaliacaoService.listarTodas();

                if (avaliacoes.isEmpty()) {
                    System.out.println("⚠️ [SANG] Banco conectado com SUCESSO, mas a tabela está vazia!");
                } else {
                    System.out.println("✅ [SANG] SUCESSO! Dados lidos corretamente do PostgreSQL:");
                    for (Avaliacao av : avaliacoes) {
                        System.out.println("   -> Militar: " + av.getAvaliado().getNGuerra()+
                                " | Nota Qualidade: " + av.getQualidadeTrabalho() +
                                " | Processo: " + av.getProcesso().getAno());
                    }
                }
            } catch (Exception e) {
                System.out.println("❌ [SANG] ERRO AO TENTAR ACESSAR OS DADOS: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("=======================================================\n");
        };
    }
}
