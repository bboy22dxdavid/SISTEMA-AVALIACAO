package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.model.Nivelamento;
import io.github.bboy22dxdavid.sang_backEnd.repository.NivelamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class NivelamentoService {
    //intanciando a interface AvaliacaoRepositoryrepository;
    private final NivelamentoRepository repository;

    //construtor da class com parametro NivelamentoRepository
    public NivelamentoService(NivelamentoRepository repository) {
        this.repository = repository;
    }

    /*======================
     *       CRUD
     *======================*/

    //metodo de salvar
    public Nivelamento salvarNivelamento(Nivelamento nivelamento) {
        // Regra de Negócio: Valida se a justificativa foi preenchida
        if (nivelamento.getJustificativa() == null || nivelamento.getJustificativa().trim().isEmpty()) {
            throw new IllegalArgumentException("A justificativa é obrigatória para registrar o nivelamento!");
        }

        validarNotasNivelamento(nivelamento);
        return repository.save(nivelamento);
    }

    //metodo de buscar por id
    public Nivelamento buscarPorAvaliacao(Long avaliacaoId) {
        return repository.findByAvaliacaoId(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Nivelamento não encontrado para esta avaliação!"));
    }

    private void validarNotasNivelamento(Nivelamento n) {
        if (isNotaInvalida(n.getQualidadeTrabalho()) || isNotaInvalida(n.getProductivityTrabalho()) ||
                isNotaInvalida(n.getConhecimentoProfissional()) || isNotaInvalida(n.getEficienciaTrabalho()) ||
                isNotaInvalida(n.getMeiosMateriais()) || isNotaInvalida(n.getPlanejamento()) ||
                isNotaInvalida(n.getJulgamento()) || isNotaInvalida(n.getResponsabilidade()) ||
                isNotaInvalida(n.getDisciplina()) || isNotaInvalida(n.getApresentacaoPessoal()) ||
                isNotaInvalida(n.getLideranca()) || isNotaInvalida(n.getIniciativa()) ||
                isNotaInvalida(n.getAdaptabilidade()) || isNotaInvalida(n.getComunicacaoOralEscrita()) ||
                isNotaInvalida(n.getRelacionamentoAmbienteTrabalho())) {

            throw new IllegalArgumentException("Todas as notas de nivelamento devem estar entre 0 e 10!");
        }
    }

    private boolean isNotaInvalida(Integer nota) {
        return nota == null || nota < 0 || nota > 10;
    }

    // metodo de bucar por id
    public Nivelamento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nivelamento não encontrado com o ID: " + id));
    }

    //metodo de deletar
    public void deletar(Long id) {
        Nivelamento nivelamento = buscarPorId(id);
        repository.delete(nivelamento);
    }
}
