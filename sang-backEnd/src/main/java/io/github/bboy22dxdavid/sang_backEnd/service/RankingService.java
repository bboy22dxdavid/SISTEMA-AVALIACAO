package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.dto.RankingResponseDTO;
import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import io.github.bboy22dxdavid.sang_backEnd.model.Nivelamento;
import io.github.bboy22dxdavid.sang_backEnd.repository.AvaliacaoRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.NivelamentoRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RankingService {

    //instancias
    private final AvaliacaoRepository avaliacaoRepository;
    private final NivelamentoRepository nivelamentoRepository;

    public RankingService(AvaliacaoRepository avaliacaoRepository, NivelamentoRepository nivelamentoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.nivelamentoRepository = nivelamentoRepository;
    }

    public List<RankingResponseDTO> gerarRankingPorProcesso(UUID processoId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByProcessoId(processoId);

        return avaliacoes.stream()
                .map(this::calcularPontuacaoMilitar)
                .sorted(Comparator.comparingInt(RankingResponseDTO::getPontuacaoTotal).reversed()
                        .thenComparingInt(RankingResponseDTO::getAntiguidade))
                .collect(Collectors.toList());
    }

    private RankingResponseDTO calcularPontuacaoMilitar(Avaliacao av) {
        Optional<Nivelamento> nivelamentoOpt = nivelamentoRepository.findByAvaliacaoId(av.getId());

        int somaTotal;
        boolean foiNivelado;

        if (nivelamentoOpt.isPresent()) {
            Nivelamento niv = nivelamentoOpt.get();
            somaTotal = somarNotasNivelamento(niv);
            foiNivelado = true;
        } else {
            somaTotal = somarNotasAvaliacao(av);
            foiNivelado = false;
        }

        return new RankingResponseDTO(
                av.getAvaliado().getId(),
                av.getId(),
                av.getAvaliado().getSaram(),
                av.getAvaliado().getPosto(),
                av.getAvaliado().getNguerra(),
                av.getAvaliado().getSecao(),
                av.getAvaliado().getAntiguidade(),
                somaTotal,
                foiNivelado
        );
    }

    private int somarNotasAvaliacao(Avaliacao a) {
        return a.getQualidadetrabalho() + a.getProdutividadetrabalho() + a.getConhecimentoprofissional()
                + a.getEficienciatrabalho() + a.getMeiosmateriais() + a.getPlanejamento()
                + a.getJulgamento() + a.getResponsabilidade() + a.getDisciplina()
                + a.getApresentacaopessoal() + a.getLideranca() + a.getIniciativa()
                + a.getAdaptabilidade() + a.getComunicacaooralescrita() + a.getRelacionamentoambientetrabalho();
    }

    private int somarNotasNivelamento(Nivelamento n) {
        return n.getQualidadetrabalho() + n.getProdutividadetrabalho() + n.getConhecimentoprofissional()
                + n.getEficienciatrabalho() + n.getMeiosmateriais() + n.getPlanejamento()
                + n.getJulgamento() + n.getResponsabilidade() + n.getDisciplina()
                + n.getApresentacaopessoal() + n.getLideranca() + n.getIniciativa()
                + n.getAdaptabilidade() + n.getComunicacaooralescrita() + n.getRelacionamentoambientetrabalho();
    }
}
