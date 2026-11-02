package io.github.bboy22dxdavid.sang_backEnd.service;



import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import io.github.bboy22dxdavid.sang_backEnd.repository.AvaliacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoService {

    /*
    * 1. Validação de Notas: As notas de todos os 15 conceitos devem estar estritamente entre 0 e 10.
    *2. Obrigatoriedade da Justificativa no Nivelamento: Conforme o escopo, se o Comando alterar qualquer
    * nota ou preencher o nivelamento, a justificativa textual é obrigatória.
    * */

    //intanciando a interface AvaliacaoRepository
    private final AvaliacaoRepository repository;

    //construtor da class com parametro AvaliacaoRepository
    public AvaliacaoService(AvaliacaoRepository repository) {
        this.repository = repository;
    }

    /*======================
    *       CRUD
    *======================*/

    //metodo de salvar
    public Avaliacao salvar(Avaliacao avaliacao) {
        validarNotas(avaliacao);
        return repository.save(avaliacao);
    }

    //metodo de listar
    public List<Avaliacao> listarTodas() {
        return repository.findAll();
    }

    //metodo de buscar por id
    public Avaliacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com o ID: " + id));
    }

    // Regra de Negócio: Garante que nenhuma nota ultrapasse os limites de 0 a 10
    private void validarNotas(Avaliacao a) {
        if (isNotaInvalida(a.getQualidadeTrabalho()) || isNotaInvalida(a.getProdutividadeTrabalho()) ||
                isNotaInvalida(a.getConhecimentoProfissional()) || isNotaInvalida(a.getEficienciaTrabalho()) ||
                isNotaInvalida(a.getMeiosMateriais()) || isNotaInvalida(a.getPlanejamento()) ||
                isNotaInvalida(a.getJulgamento()) || isNotaInvalida(a.getResponsabilidade()) ||
                isNotaInvalida(a.getDisciplina()) || isNotaInvalida(a.getApresentacaoPessoal()) ||
                isNotaInvalida(a.getLideranca()) || isNotaInvalida(a.getIniciativa()) ||
                isNotaInvalida(a.getAdaptabilidade()) || isNotaInvalida(a.getComunicacaoOralEscrita()) ||
                isNotaInvalida(a.getRelacionamentoAmbienteTrabalho())) {

            throw new IllegalArgumentException("Todas as notas dos conceitos devem estar entre 0 e 10!");
        }
    }

    private boolean isNotaInvalida(Integer nota) {
        return nota == null || nota < 0 || nota > 10;
    }
}
