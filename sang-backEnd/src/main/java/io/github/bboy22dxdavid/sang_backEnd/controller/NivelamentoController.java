package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.model.Nivelamento;
import io.github.bboy22dxdavid.sang_backEnd.service.NivelamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nivelamento")
@CrossOrigin(origins = "*")// Permite a comunicação direta com o Angular
public class NivelamentoController {


    //intancia do NivelamentoService
    private final NivelamentoService service;

    public NivelamentoController(NivelamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Nivelamento> criar(@RequestBody Nivelamento nivelamento) {
        Nivelamento salvo = service.salvar(nivelamento);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Nivelamento>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nivelamento> buscarPorId(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.buscarPorId(UUID.fromString(id)));
    }

    @GetMapping("/avaliacao/{avaliacaoId}")
    public ResponseEntity<Nivelamento> buscarPorAvaliacao(@PathVariable("avaliacaoId") String avaliacaoId) {
        return ResponseEntity.ok(service.buscarPorAvaliacao(UUID.fromString(avaliacaoId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") String id) {
        service.deletar(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
