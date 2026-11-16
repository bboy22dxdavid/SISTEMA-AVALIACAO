package io.github.bboy22dxdavid.sang_backEnd.service;



import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import io.github.bboy22dxdavid.sang_backEnd.model.Avaliado;
import io.github.bboy22dxdavid.sang_backEnd.model.Processo;
import io.github.bboy22dxdavid.sang_backEnd.model.User;
import io.github.bboy22dxdavid.sang_backEnd.repository.AvaliacaoRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.AvaliadoRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.ProcessoRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AvaliacaoService {

    /*
    * 1. Validação de Notas: As notas de todos os 15 conceitos devem estar estritamente entre 0 e 10.
    *2. Obrigatoriedade da Justificativa no Nivelamento: Conforme o escopo, se o Comando alterar qualquer
    * nota ou preencher o nivelamento, a justificativa textual é obrigatória.
    * */

    //intanciando a interface AvaliacaoRepository
    private final AvaliacaoRepository avaliacaoRepository;
    private final AvaliadoRepository avaliadoRepository;
    private final UserRepository userRepository;
    private final ProcessoRepository processoRepository;

    //construtor da class com parametro AvaliacaoRepository
    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository,
       AvaliadoRepository avaliadoRepository,UserRepository userRepository,
       ProcessoRepository processoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.avaliadoRepository = avaliadoRepository;
        this.userRepository = userRepository;
        this.processoRepository = processoRepository;
    }


    /*======================
    *       CRUD
    *======================*/

    //metodo de salvar
    public Avaliacao salvar(Avaliacao avaliacao) {
        if (avaliacao.getAvaliado() == null || avaliacao.getAvaliado().getId() == null) {
            throw new IllegalArgumentException("O ID do avaliado é obrigatório.");
        }
        if (avaliacao.getAvaliador() == null || avaliacao.getAvaliador().getId() == null) {
            throw new IllegalArgumentException("O ID do avaliador é obrigatório.");
        }
        if (avaliacao.getProcesso() == null || avaliacao.getProcesso().getId() == null) {
            throw new IllegalArgumentException("O ID do processo é obrigatório.");
        }

        Avaliado avaliado = avaliadoRepository.findById(avaliacao.getAvaliado().getId())
                .orElseThrow(() -> new RuntimeException("Avaliado não encontrado."));

        User avaliador = userRepository.findById(avaliacao.getAvaliador().getId())
                .orElseThrow(() -> new RuntimeException("Avaliador não encontrado."));

        Processo processo = processoRepository.findById(avaliacao.getProcesso().getId())
                .orElseThrow(() -> new RuntimeException("Processo não encontrado."));

        // Regra 1: Verifica se o usuário tem permissão para avaliar
        if (avaliador.getRole() == null || !"ROLE_AVALIADOR".equals(avaliador.getRole().getRole())) {
            throw new IllegalArgumentException("Apenas usuários com perfil ROLE_AVALIADOR podem lançar avaliações.");
        }

        // Regra 2: Bloqueio de autoavaliação (caso o graduado esteja nas duas tabelas)
        if (avaliador.getSaram().equals(avaliado.getSaram())) {
            throw new IllegalArgumentException("Autoavaliação não permitida: o avaliador possui o mesmo SARAM do avaliado.");
        }

        // Regra 3: Impede duas avaliações para o mesmo militar no mesmo processo anual
        if (avaliacaoRepository.existsByAvaliadoIdAndProcessoId(avaliado.getId(), processo.getId())) {
            throw new IllegalArgumentException("Este militar já foi avaliado neste processo.");
        }

        // Regra 4: Valida notas de 0 a 10
        validarNotas(avaliacao);

        avaliacao.setAvaliado(avaliado);
        avaliacao.setAvaliador(avaliador);
        avaliacao.setProcesso(processo);

        return avaliacaoRepository.save(avaliacao);
    }

    //metodo de listar
    public List<Avaliacao> listarTodas() {
        return avaliacaoRepository.findAll();
    }

    //metodo de buscar por processo id
    public List<Avaliacao> listarPorProcesso(UUID processoId) {
        return avaliacaoRepository.findByProcessoId(processoId);
    }

    //busca a avaliacao por id

    public Avaliacao buscarPorId(UUID id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com o ID: " + id));
    }

    //metodo que deleta a nota
    public void deletar(UUID id) {
        Avaliacao avaliacao = buscarPorId(id);
        avaliacaoRepository.delete(avaliacao);
    }

    // metodo que valida a nota
    private void validarNotas(Avaliacao a) {
        if (isNotaInvalida(a.getQualidadetrabalho()) || isNotaInvalida(a.getProdutividadetrabalho()) ||
                isNotaInvalida(a.getConhecimentoprofissional()) || isNotaInvalida(a.getEficienciatrabalho()) ||
                isNotaInvalida(a.getMeiosmateriais()) || isNotaInvalida(a.getPlanejamento()) ||
                isNotaInvalida(a.getJulgamento()) || isNotaInvalida(a.getResponsabilidade()) ||
                isNotaInvalida(a.getDisciplina()) || isNotaInvalida(a.getApresentacaopessoal()) ||
                isNotaInvalida(a.getLideranca()) || isNotaInvalida(a.getIniciativa()) ||
                isNotaInvalida(a.getAdaptabilidade()) || isNotaInvalida(a.getComunicacaooralescrita()) ||
                isNotaInvalida(a.getRelacionamentoambientetrabalho())) {

            throw new IllegalArgumentException("Todas as 15 notas dos conceitos devem ser preenchidas com valores entre 0 e 10.");
        }
    }

    private boolean isNotaInvalida(Integer nota) {
        return nota == null || nota < 0 || nota > 10;
    }
}
