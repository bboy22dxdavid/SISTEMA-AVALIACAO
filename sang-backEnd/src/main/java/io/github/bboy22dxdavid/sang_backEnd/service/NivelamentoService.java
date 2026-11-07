package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import io.github.bboy22dxdavid.sang_backEnd.model.Nivelamento;
import io.github.bboy22dxdavid.sang_backEnd.model.User;
import io.github.bboy22dxdavid.sang_backEnd.repository.AvaliacaoRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.NivelamentoRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NivelamentoService {

    //instaciando as class
    private final NivelamentoRepository nivelamentoRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final UserRepository userRepository;

    public NivelamentoService(NivelamentoRepository nivelamentoRepository,
         AvaliacaoRepository avaliacaoRepository,UserRepository userRepository) {
        this.nivelamentoRepository = nivelamentoRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.userRepository = userRepository;
    }

    public Nivelamento salvar(Nivelamento nivelamento) {

        //verificando os ids
        if (nivelamento.getAvaliacao() == null || nivelamento.getAvaliacao().getId() == null) {
            throw new IllegalArgumentException("O ID da avaliação é obrigatório.");
        }
        if (nivelamento.getRevisor() == null || nivelamento.getRevisor().getId() == null) {
            throw new IllegalArgumentException("O ID do revisor é obrigatório.");
        }

        Avaliacao avaliacao = avaliacaoRepository.findById(nivelamento.getAvaliacao().getId())
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada."));

        User revisor = userRepository.findById(nivelamento.getRevisor().getId())
                .orElseThrow(() -> new RuntimeException("Revisor não encontrado."));

        // Regra 1: Apenas oficiais superiores (ROLE_REVISOR) podem nivelar
        if (revisor.getRole() == null || !"ROLE_REVISOR".equals(revisor.getRole().getRole())) {
            throw new IllegalArgumentException("Apenas usuários com perfil ROLE_REVISOR podem registrar nivelamentos.");
        }

        // Regra 2: Garante relacionamento 1:1 (uma avaliação não pode ter dois nivelamentos)
        if (nivelamentoRepository.existsByAvaliacaoId(avaliacao.getId())) {
            throw new IllegalArgumentException("Esta avaliação já possui um nivelamento registrado.");
        }

        // Regra 3: Justificativa obrigatória
        if (nivelamento.getJustificativa() == null || nivelamento.getJustificativa().trim().isEmpty()) {
            throw new IllegalArgumentException("A justificativa é obrigatória para registrar o nivelamento.");
        }

        // Regra 4: Valida notas de 0 a 10
        validarNotas(nivelamento);

        nivelamento.setAvaliacao(avaliacao);
        nivelamento.setRevisor(revisor);

        return nivelamentoRepository.save(nivelamento);
    }

    public List<Nivelamento> listarTodos() {
        return nivelamentoRepository.findAll();
    }

    public Nivelamento buscarPorId(UUID id) {
        return nivelamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nivelamento não encontrado com o ID: " + id));
    }

    public Nivelamento buscarPorAvaliacao(UUID avaliacaoId) {
        return nivelamentoRepository.findByAvaliacaoId(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Nivelamento não encontrado para a avaliação informada."));
    }

    public void deletar(UUID id) {
        Nivelamento nivelamento = buscarPorId(id);
        nivelamentoRepository.delete(nivelamento);
    }

    private void validarNotas(Nivelamento n) {
        if (isNotaInvalida(n.getQualidadetrabalho()) || isNotaInvalida(n.getProdutividadetrabalho()) ||
                isNotaInvalida(n.getConhecimentoprofissional()) || isNotaInvalida(n.getEficienciatrabalho()) ||
                isNotaInvalida(n.getMeiosmateriais()) || isNotaInvalida(n.getPlanejamento()) ||
                isNotaInvalida(n.getJulgamento()) || isNotaInvalida(n.getResponsabilidade()) ||
                isNotaInvalida(n.getDisciplina()) || isNotaInvalida(n.getApresentacaopessoal()) ||
                isNotaInvalida(n.getLideranca()) || isNotaInvalida(n.getIniciativa()) ||
                isNotaInvalida(n.getAdaptabilidade()) || isNotaInvalida(n.getComunicacaooralescrita()) ||
                isNotaInvalida(n.getRelacionamentoambientetrabalho())) {

            throw new IllegalArgumentException("Todas as 15 notas de nivelamento devem estar entre 0 e 10.");
        }
    }

    private boolean isNotaInvalida(Integer nota) {
        return nota == null || nota < 0 || nota > 10;
    }
}
