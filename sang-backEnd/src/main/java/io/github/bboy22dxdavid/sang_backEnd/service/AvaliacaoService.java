package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.dto.AvaliacaoRequest;
import io.github.bboy22dxdavid.sang_backEnd.model.*;
import io.github.bboy22dxdavid.sang_backEnd.repository.AvaliacaoRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.DesignacaoSetorRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ProcessoService processoService;
    private final EfetivoService efetivoService;
    private final UserRepository userRepository;
    private final DesignacaoSetorRepository designacaoRepository;
    private final HierarquiaValidator hierarquiaValidator;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository,
                            ProcessoService processoService,
                            EfetivoService efetivoService,
                            UserRepository userRepository,
                            DesignacaoSetorRepository designacaoRepository,
                            HierarquiaValidator hierarquiaValidator) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.processoService = processoService;
        this.efetivoService = efetivoService;
        this.userRepository = userRepository;
        this.designacaoRepository = designacaoRepository;
        this.hierarquiaValidator = hierarquiaValidator;
    }

    public List<Avaliacao> listarPorProcesso(UUID processoId) {
        return avaliacaoRepository.findByProcessoId(processoId);
    }

    public Avaliacao buscarPorId(UUID id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ficha de avaliação não encontrada."));
    }

    @Transactional
    public Avaliacao registrar(AvaliacaoRequest request, String usernameAvaliador) {
        // 1. Validar Processo Aberto
        Processo processo = processoService.buscarPorId(request.processoId());
        if (processo.getStatus() == StatusProcesso.ENCERRADO) {
            throw new IllegalArgumentException("Este processo anual já está ENCERRADO e não aceita novas avaliações.");
        }

        // 2. Buscar Avaliador logado e Militar Avaliado (já valida se é CARREIRA e Ativo)
        User avaliador = userRepository.findByUsernameAndAtivoTrue(usernameAvaliador)
                .orElseThrow(() -> new EntityNotFoundException("Avaliador não encontrado ou inativo."));

        Efetivo avaliado = efetivoService.buscarElegivelPorId(request.avaliadoId());

        // 3. Validar se o Avaliador está designado para a Seção do Avaliado neste Processo
        DesignacaoSetor designacao = designacaoRepository
                .findByProcessoIdAndSecao(processo.getId(), avaliado.getSecao())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Não há avaliador designado para a seção '" + avaliado.getSecao() + "' neste processo."));

        if (!designacao.getAvaliador().getId().equals(avaliador.getId())) {
            throw new IllegalArgumentException(
                    "Acesso negado: você não é o avaliador designado para a seção '" + avaliado.getSecao() + "'.");
        }

        // 4. Validar Precedência Hierárquica (Antiguidade)
        hierarquiaValidator.validarPrecedencia(avaliador, avaliado);

        // 5. Impedir avaliação duplicada no mesmo processo
        if (avaliacaoRepository.existsByProcessoIdAndAvaliadoId(processo.getId(), avaliado.getId())) {
            throw new IllegalArgumentException("Este militar já foi avaliado neste processo anual. A ficha é imutável.");
        }

        // 6. Validar os 15 critérios na escala estrita de 1 a 5
        int notaTotal = calcularEValidarCriterios(request.criterios());

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setProcesso(processo);
        avaliacao.setAvaliado(avaliado);
        avaliacao.setAvaliador(avaliador);
        avaliacao.setCriterios(request.criterios());
        avaliacao.setNotaTotal(notaTotal);
        avaliacao.setJustificativa(request.justificativa().trim());
        avaliacao.setConceitoMoral(request.conceitoMoral().trim());
        avaliacao.setConceitoProfissional(request.conceitoProfissional().trim());

        return avaliacaoRepository.save(avaliacao);
    }

    private int calcularEValidarCriterios(Map<String, Integer> criterios) {
        if (criterios == null || criterios.size() != 15) {
            throw new IllegalArgumentException("A ficha deve conter exatamente os 15 critérios técnicos obrigatórios.");
        }

        int soma = 0;
        for (Map.Entry<String, Integer> item : criterios.entrySet()) {
            Integer nota = item.getValue();
            if (nota == null || nota < 1 || nota > 5) {
                throw new IllegalArgumentException(
                        "Nota inválida no critério '" + item.getKey() + "'. A escala permitida é de 1 a 5.");
            }
            soma += nota;
        }
        return soma;
    }

}
