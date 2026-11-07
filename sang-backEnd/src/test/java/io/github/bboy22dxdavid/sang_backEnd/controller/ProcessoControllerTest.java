package io.github.bboy22dxdavid.sang_backEnd.controller;


import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import io.github.bboy22dxdavid.sang_backEnd.model.User;
import io.github.bboy22dxdavid.sang_backEnd.repository.AvaliacaoRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.ProcessoRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.UserRepository;
import io.github.bboy22dxdavid.sang_backEnd.service.ProcessoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // DIZ AO SPRING PARA USAR AS CONFIGURAÇÕES DO APPLICATION-TEST.YML
public class ProcessoControllerTest {
    //@Autowired
    private ProcessoRepository repository;

    private ProcessoService service;

    //@Autowired
    private AvaliacaoRepository repositoryAv; // Garanta que o tipo aqui é AvaliacaoRepository

    @Test
    public void listarTest() {
        // Criando a lista de avaliações (trazendo o relacionamento com o militar/avaliado)
        List<Avaliacao> lista = repositoryAv.findAll();

        // Criando laço para percorrer toda a lista e imprimindo o ranking formatado
        lista.forEach(avaliacao -> {
            // Calcula a média das 15 notas
            double media = (
                    avaliacao.getQualidadeTrabalho() + avaliacao.getProdutividadeTrabalho() +
                            avaliacao.getConhecimentoProfissional() + avaliacao.getEficienciaTrabalho() +
                            avaliacao.getMeiosMateriais() + avaliacao.getPlanejamento() +
                            avaliacao.getJulgamento() + avaliacao.getResponsabilidade() +
                            avaliacao.getDisciplina() + avaliacao.getApresentacaoPessoal() +
                            avaliacao.getLideranca() + avaliacao.getIniciativa() +
                            avaliacao.getAdaptabilidade() + avaliacao.getComunicacaoOralEscrita() +
                            avaliacao.getRelacionamentoAmbienteTrabalho()
            ) / 15.0;

            // Imprime no console os dados do militar e a nota final do ranking
            System.out.printf("Militar: %s (%s) | Ano: %d | Nota Final Ranking: %.2f%n",
                    avaliacao.getAvaliado().getNGuerra(),
                    avaliacao.getAvaliado().getPosto(),
                    avaliacao.getProcesso().getAno(),
                    media
            );
        });
    }

    @Autowired
    private UserRepository userRepository; // Certifique-se de declarar o repositório correto aqui

    @Test
    public void listarUsuariosTest() {
        // Buscando a lista de todos os usuários do banco
        List<User> listaUsuarios = userRepository.findAll();

        // Percorrendo a lista e imprimindo as informações no terminal
        listaUsuarios.forEach(usuario -> {
            // Captura o nome da role diretamente, tratando caso ela seja nula
            //String papel = (usuario.getRole() != null) ? usuario.getRole().getId(): "SEM PERMISSÃO";

            // Imprime formatado no terminal
            System.out.printf("ID: %d | Usuário: %s | Permissão: %s%n",
                    usuario.getId(),
                    usuario.getUsername()

            );
        });
    }

}
