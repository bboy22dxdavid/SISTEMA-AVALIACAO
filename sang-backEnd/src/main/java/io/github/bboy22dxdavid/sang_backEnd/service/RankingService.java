package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.dto.RankingResponse;
import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import io.github.bboy22dxdavid.sang_backEnd.model.Efetivo;
import io.github.bboy22dxdavid.sang_backEnd.model.Nivelamento;
import io.github.bboy22dxdavid.sang_backEnd.repository.AvaliacaoRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.NivelamentoRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RankingService {
    private final EfetivoService efetivoService;
    private final ProcessoService processoService;
    private final AvaliacaoRepository avaliacaoRepository;
    private final NivelamentoRepository nivelamentoRepository;

    public RankingService(EfetivoService efetivoService,
                          ProcessoService processoService,
                          AvaliacaoRepository avaliacaoRepository,
                          NivelamentoRepository nivelamentoRepository) {
        this.efetivoService = efetivoService;
        this.processoService = processoService;
        this.avaliacaoRepository = avaliacaoRepository;
        this.nivelamentoRepository = nivelamentoRepository;
    }

    public List<RankingResponse> gerarRankingPorProcesso(UUID processoId) {
        processoService.buscarPorId(processoId); // Garante que o processo existe

        List<Efetivo> elegiveis = efetivoService.listarElegiveis();

        Map<UUID, Avaliacao> avaliacoesPorMilitar = avaliacaoRepository.findByProcessoId(processoId)
                .stream()
                .collect(Collectors.toMap(a -> a.getAvaliado().getId(), Function.identity()));

        Map<UUID, Nivelamento> nivelamentosPorAvaliacao = nivelamentoRepository.findByAvaliacaoProcessoId(processoId)
                .stream()
                .collect(Collectors.toMap(n -> n.getAvaliacao().getId(), Function.identity()));

        List<RankingResponse> listaNaoOrdenada = new ArrayList<>();

        for (Efetivo militar : elegiveis) {
            Avaliacao avaliacao = avaliacoesPorMilitar.get(militar.getId());
            Nivelamento nivelamento = (avaliacao != null) ? nivelamentosPorAvaliacao.get(avaliacao.getId()) : null;

            Integer notaOriginal = (avaliacao != null) ? avaliacao.getNotaTotal() : 0;
            Integer notaFinal = (nivelamento != null) ? nivelamento.getNotaTotalRevisada() : notaOriginal;

            listaNaoOrdenada.add(new RankingResponse(
                    0,
                    militar.getId(),
                    avaliacao != null ? avaliacao.getId() : null,
                    nivelamento != null ? nivelamento.getId() : null,
                    militar.getSaram(),
                    militar.getPosto(),
                    militar.getNomeGuerra(),
                    militar.getFotoUrl(),
                    militar.getSecao(),
                    militar.getAntiguidade(),
                    notaOriginal,
                    notaFinal,
                    avaliacao != null,
                    nivelamento != null
            ));
        }

        // Ordena: 1º Avaliados antes de pendentes | 2º Maior Nota Final | 3º Menor Antiguidade (mais antigo)
        listaNaoOrdenada.sort(
                Comparator.comparing(RankingResponse::avaliado).reversed()
                        .thenComparing(RankingResponse::notaFinal, Comparator.reverseOrder())
                        .thenComparing(RankingResponse::antiguidade)
        );

        // Atribui a posição oficial sequencial (1º, 2º, 3º...)
        List<RankingResponse> rankingOficial = new ArrayList<>();
        for (int i = 0; i < listaNaoOrdenada.size(); i++) {
            RankingResponse item = listaNaoOrdenada.get(i);
            rankingOficial.add(new RankingResponse(
                    i + 1,
                    item.avaliadoId(),
                    item.avaliacaoId(),
                    item.nivelamentoId(),
                    item.saram(),
                    item.posto(),
                    item.nomeGuerra(),
                    item.fotoUrl(),
                    item.secao(),
                    item.antiguidade(),
                    item.notaOriginal(),
                    item.notaFinal(),
                    item.avaliado(),
                    item.nivelado()
            ));
        }

        return rankingOficial;
    }
}
