package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.dto.RankingResponseDTO;
import io.github.bboy22dxdavid.sang_backEnd.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ranking")
@CrossOrigin(origins = "*")
public class RankingController {
    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/processo/{processoId}")
    public ResponseEntity<List<RankingResponseDTO>> obterRankingPorProcesso(
            @PathVariable("processoId") String processoId) {
        return ResponseEntity.ok(rankingService.gerarRankingPorProcesso(UUID.fromString(processoId)));
    }
}
