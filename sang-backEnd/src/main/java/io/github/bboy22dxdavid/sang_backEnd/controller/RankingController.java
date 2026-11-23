package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.dto.RankingResponse;
import io.github.bboy22dxdavid.sang_backEnd.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {
    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/processo/{processoId}")
    public ResponseEntity<List<RankingResponse>> obterRankingPorProcesso(@PathVariable UUID processoId) {
        return ResponseEntity.ok(rankingService.gerarRankingPorProcesso(processoId));
    }
}
