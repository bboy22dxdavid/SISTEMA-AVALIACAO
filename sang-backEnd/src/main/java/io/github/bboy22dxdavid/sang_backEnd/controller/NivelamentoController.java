package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.dto.NivelamentoRequest;
import io.github.bboy22dxdavid.sang_backEnd.model.Nivelamento;
import io.github.bboy22dxdavid.sang_backEnd.service.NivelamentoService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/nivelamentos")
public class NivelamentoController {
    private final NivelamentoService nivelamentoService;

    public NivelamentoController(NivelamentoService nivelamentoService) {
        this.nivelamentoService = nivelamentoService;
    }

    @GetMapping("/avaliacao/{avaliacaoId}")
    public ResponseEntity<Nivelamento> buscarPorAvaliacao(@PathVariable UUID avaliacaoId) {
        return ResponseEntity.ok(nivelamentoService.buscarPorAvaliacaoId(avaliacaoId));
    }

    @PostMapping
    @PreAuthorize("hasRole('REVIEWER')")
    public ResponseEntity<Nivelamento> registrar(
            @Valid @RequestBody NivelamentoRequest request,
            Authentication authentication) {
        Nivelamento salvo = nivelamentoService.registrar(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}
