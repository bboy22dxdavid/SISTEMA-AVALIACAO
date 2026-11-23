package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.dto.ProcessoRequest;
import io.github.bboy22dxdavid.sang_backEnd.model.Processo;
import io.github.bboy22dxdavid.sang_backEnd.service.ProcessoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/processos")
public class ProcessoController {

    private final ProcessoService processoService;

    public ProcessoController(ProcessoService processoService) {
        this.processoService = processoService;
    }

    @GetMapping
    public ResponseEntity<List<Processo>> listarTodos() {
        return ResponseEntity.ok(processoService.listarTodos());
    }

    @PostMapping
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<Processo> criar(@Valid @RequestBody ProcessoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(processoService.criar(request));
    }

    @PutMapping("/{id}/encerrar")
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<Processo> encerrar(@PathVariable UUID id) {
        return ResponseEntity.ok(processoService.encerrar(id));
    }
}
