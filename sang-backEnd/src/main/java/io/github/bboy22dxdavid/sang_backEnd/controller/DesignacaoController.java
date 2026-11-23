package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.dto.DesignacaoRequest;
import io.github.bboy22dxdavid.sang_backEnd.model.DesignacaoSetor;
import io.github.bboy22dxdavid.sang_backEnd.service.DesignacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/designacoes")
public class DesignacaoController {
    private final DesignacaoService designacaoService;

    public DesignacaoController(DesignacaoService designacaoService) {
        this.designacaoService = designacaoService;
    }

    @GetMapping("/processo/{processoId}")
    public ResponseEntity<List<DesignacaoSetor>> listarPorProcesso(@PathVariable UUID processoId) {
        return ResponseEntity.ok(designacaoService.listarPorProcesso(processoId));
    }

    @GetMapping("/processo/{processoId}/avaliador/{avaliadorId}")
    public ResponseEntity<List<DesignacaoSetor>> listarPorAvaliador(
            @PathVariable UUID processoId,
            @PathVariable UUID avaliadorId) {
        return ResponseEntity.ok(designacaoService.listarPorProcessoEAvaliador(processoId, avaliadorId));
    }

    @PostMapping
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<DesignacaoSetor> designar(@Valid @RequestBody DesignacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(designacaoService.designar(request));
    }
}
