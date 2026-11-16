package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.model.Avaliado;
import io.github.bboy22dxdavid.sang_backEnd.service.AvaliadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/avaliados")
@CrossOrigin(origins = "*")
public class AvaliadoController {

    private final AvaliadoService service;

    public AvaliadoController(AvaliadoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Avaliado> criar(@RequestBody Avaliado avaliado) {
        Avaliado salvo = service.salvar(avaliado);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Avaliado>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Avaliado>> listarAtivos() {
        return ResponseEntity.ok(service.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliado> buscarPorId(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.buscarPorId(UUID.fromString(id)));
    }

    @GetMapping("/saram/{saram}")
    public ResponseEntity<Avaliado> buscarPorSaram(@PathVariable("saram") String saram) {
        return ResponseEntity.ok(service.buscarPorSaram(saram));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avaliado> atualizar(@PathVariable("id") String id, @RequestBody Avaliado avaliado) {
        return ResponseEntity.ok(service.atualizar(UUID.fromString(id), avaliado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") String id) {
        service.deletar(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
