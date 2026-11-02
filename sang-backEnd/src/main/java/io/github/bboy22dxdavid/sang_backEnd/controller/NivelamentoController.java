package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.model.Nivelamento;
import io.github.bboy22dxdavid.sang_backEnd.service.NivelamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nivelamento")
@CrossOrigin(origins = "*")// Permite a comunicação direta com o Angular
public class NivelamentoController {
    //Mapeia o endpoint /api/nivelamento

    //intancia do NivelamentoService
    private final NivelamentoService service;

    // construtor que recebe a NivelamentoService
    public NivelamentoController(NivelamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Nivelamento> criarNivelamento(@RequestBody Nivelamento nivelamento) {
        Nivelamento salvo = service.salvarNivelamento(nivelamento);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    @GetMapping("/avaliacao/{avaliacaoId}")
    public ResponseEntity<Nivelamento> buscarPorAvaliacao(@PathVariable Long avaliacaoId) {
        Nivelamento nivelamento = service.buscarPorAvaliacao(avaliacaoId);
        return ResponseEntity.ok(nivelamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
