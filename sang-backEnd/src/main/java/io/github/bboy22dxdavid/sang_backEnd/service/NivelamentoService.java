package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.dto.NivelamentoRequest;
import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import io.github.bboy22dxdavid.sang_backEnd.model.Nivelamento;
import io.github.bboy22dxdavid.sang_backEnd.model.StatusProcesso;
import io.github.bboy22dxdavid.sang_backEnd.model.User;
import io.github.bboy22dxdavid.sang_backEnd.repository.NivelamentoRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class NivelamentoService {

    private final NivelamentoRepository nivelamentoRepository;
    private final AvaliacaoService avaliacaoService;
    private final UserRepository userRepository;

    public NivelamentoService(NivelamentoRepository nivelamentoRepository,
                              AvaliacaoService avaliacaoService,
                              UserRepository userRepository) {
        this.nivelamentoRepository = nivelamentoRepository;
        this.avaliacaoService = avaliacaoService;
        this.userRepository = userRepository;
    }

    public Nivelamento buscarPorAvaliacaoId(UUID avaliacaoId) {
        return nivelamentoRepository.findByAvaliacaoId(avaliacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Nivelamento não encontrado para esta avaliação."));
    }

    @Transactional
    public Nivelamento registrar(NivelamentoRequest request, String usernameRevisor) {
        Avaliacao avaliacao = avaliacaoService.buscarPorId(request.avaliacaoId());

        if (avaliacao.getProcesso().getStatus() == StatusProcesso.ENCERRADO) {
            throw new IllegalArgumentException("Este processo anual já está ENCERRADO e não aceita revisões.");
        }

        User revisor = userRepository.findByUsernameAndAtivoTrue(usernameRevisor)
                .orElseThrow(() -> new EntityNotFoundException("Revisor não encontrado ou inativo."));

        if (!"CEL".equalsIgnoreCase(revisor.getCodPosto())) {
            throw new IllegalArgumentException("Regra de Negócio: Apenas militares no posto de Coronel (CEL) podem realizar o nivelamento.");
        }

        if (nivelamentoRepository.existsByAvaliacaoId(avaliacao.getId())) {
            throw new IllegalArgumentException("Esta ficha de avaliação já foi nivelada pelo Comando.");
        }

        int notaRevisada = calcularEValidarCriterios(request.criterios());

        Nivelamento nivelamento = new Nivelamento();
        nivelamento.setAvaliacao(avaliacao);
        nivelamento.setRevisor(revisor);
        nivelamento.setCriterios(request.criterios());
        nivelamento.setNotaTotalRevisada(notaRevisada);
        nivelamento.setJustificativaRevisao(
                request.justificativaRevisao() != null ? request.justificativaRevisao().trim() : null
        );
        nivelamento.setConceitoMoral(request.conceitoMoral().trim());
        nivelamento.setConceitoProfissional(request.conceitoProfissional().trim());

        return nivelamentoRepository.save(nivelamento);
    }

    private int calcularEValidarCriterios(Map<String, Integer> criterios) {
        if (criterios == null || criterios.size() != 15) {
            throw new IllegalArgumentException("A revisão deve conter exatamente os 15 critérios técnicos obrigatórios.");
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
