package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import io.github.bboy22dxdavid.sang_backEnd.service.AvaliacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/avaliacao")
@CrossOrigin(origins = "*") // Permite a comunicação direta com o Angular
public class AvaliacaoController {

    private final AvaliacaoService service;

    public AvaliacaoController(AvaliacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Avaliacao> criar(@RequestBody Avaliacao avaliacao) {
        Avaliacao salva = service.salvar(avaliacao);
        return new ResponseEntity<>(salva, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Avaliacao>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/processo/{processoId}")
    public ResponseEntity<List<Avaliacao>> listarPorProcesso(@PathVariable("processoId") String processoId) {
        return ResponseEntity.ok(service.listarPorProcesso(UUID.fromString(processoId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscarPorId(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.buscarPorId(UUID.fromString(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") String id) {
        service.deletar(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
