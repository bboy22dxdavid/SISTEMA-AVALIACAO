package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.model.Efetivo;
import io.github.bboy22dxdavid.sang_backEnd.service.EfetivoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/efetivo")
public class EfetivoController {
    private final EfetivoService efetivoService;

    public EfetivoController(EfetivoService efetivoService) {
        this.efetivoService = efetivoService;
    }

    @GetMapping
    public ResponseEntity<List<Efetivo>> listarElegiveis(@RequestParam(required = false) String secao) {
        if (secao != null && !secao.isBlank()) {
            return ResponseEntity.ok(efetivoService.listarElegiveisPorSecao(secao));
        }
        return ResponseEntity.ok(efetivoService.listarElegiveis());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Efetivo> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(efetivoService.buscarElegivelPorId(id));
    }

    @GetMapping("/saram/{saram}")
    public ResponseEntity<Efetivo> buscarPorSaram(@PathVariable String saram) {
        return ResponseEntity.ok(efetivoService.buscarElegivelPorSaram(saram));
    }

}
