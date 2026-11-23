package io.github.bboy22dxdavid.sang_backEnd.dto;

import java.util.UUID;

public record RankingResponse(
        Integer posicao,
        UUID avaliadoId,
        UUID avaliacaoId,
        UUID nivelamentoId,
        String saram,
        String posto,
        String nomeGuerra,
        String fotoUrl,
        String secao,
        Integer antiguidade,
        Integer notaOriginal,
        Integer notaFinal,
        boolean avaliado,
        boolean nivelado
) { }
