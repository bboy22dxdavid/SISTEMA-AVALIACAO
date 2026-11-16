package io.github.bboy22dxdavid.sang_backEnd.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RankingResponseDTO {

    private UUID avaliadoId;
    private UUID avaliacaoId;
    private String saram;
    private String posto;
    private String nguerra;
    private String secao;
    private Integer antiguidade;
    private Integer pontuacaoTotal;
    private boolean nivelado;

    public RankingResponseDTO() {}

    public RankingResponseDTO(UUID avaliadoId, UUID avaliacaoId, String saram, String posto, String nguerra,
                              String secao, Integer antiguidade, Integer pontuacaoTotal, boolean nivelado) {
        this.avaliadoId = avaliadoId;
        this.avaliacaoId = avaliacaoId;
        this.saram = saram;
        this.posto = posto;
        this.nguerra = nguerra;
        this.secao = secao;
        this.antiguidade = antiguidade;
        this.pontuacaoTotal = pontuacaoTotal;
        this.nivelado = nivelado;
    }
}
