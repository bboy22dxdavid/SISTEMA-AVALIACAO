package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.dto.AvaliacaoRequest;
import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import io.github.bboy22dxdavid.sang_backEnd.service.AvaliacaoService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {
    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @GetMapping("/processo/{processoId}")
    public ResponseEntity<List<Avaliacao>> listarPorProcesso(@PathVariable UUID processoId) {
        return ResponseEntity.ok(avaliacaoService.listarPorProcesso(processoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(avaliacaoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('EVALUATOR')")
    public ResponseEntity<Avaliacao> registrar(
            @Valid @RequestBody AvaliacaoRequest request,
            Authentication authentication) {
        String usernameAvaliador = authentication.getName();
        Avaliacao salva = avaliacaoService.registrar(request, usernameAvaliador);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }
}
