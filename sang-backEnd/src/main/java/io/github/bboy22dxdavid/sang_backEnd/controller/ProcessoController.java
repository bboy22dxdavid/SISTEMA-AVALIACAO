package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.model.Processo;
import io.github.bboy22dxdavid.sang_backEnd.service.ProcessoService;
import jakarta.validation.Valid;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/* =========================================================
 * class ProcessoController camada de entrada dos endpoints
 * mapeamos a URI solicitada no escopo original (/api/processos)
 * =========================================================
 * */

@RestController
@RequestMapping("/api/processos")
@CrossOrigin(origins = "*")//Permite que o Angular acesse o Back-end futuramente
public class ProcessoController {

    //@Autowired
    private final ProcessoService service;

    public ProcessoController(ProcessoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Processo> criar(@RequestBody Processo processo) {
        Processo salvo = service.salvar(processo);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Processo>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }



    @GetMapping("/{id}")
    public ResponseEntity<Processo> buscarPorId(@PathVariable("id")  String id) {
        return ResponseEntity.ok(service.buscarPorId(UUID.fromString(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Processo> atualizar(@PathVariable ("id") String id, @RequestBody Processo processo) {
        return ResponseEntity.ok(service.atualizar(UUID.fromString(id), processo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") String id) {
        service.deletar(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
