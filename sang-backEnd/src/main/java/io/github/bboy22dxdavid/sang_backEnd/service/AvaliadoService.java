package io.github.bboy22dxdavid.sang_backEnd.service;


import io.github.bboy22dxdavid.sang_backEnd.model.Avaliado;
import io.github.bboy22dxdavid.sang_backEnd.repository.AvaliadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AvaliadoService {

    private final AvaliadoRepository repository;

    public AvaliadoService(AvaliadoRepository repository) {
        this.repository = repository;
    }

    public Avaliado salvar(Avaliado avaliado) {
        if (repository.findBySaram(avaliado.getSaram()).isPresent()) {
            throw new IllegalArgumentException("Já existe um avaliado cadastrado com o SARAM: " + avaliado.getSaram());
        }
        if (avaliado.getAtivo() == null) {
            avaliado.setAtivo(true);
        }
        return repository.save(avaliado);
    }

    public List<Avaliado> listarTodos() {
        return repository.findAll();
    }

    public List<Avaliado> listarAtivos() {
        return repository.findByAtivoTrue();
    }

    public Avaliado buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliado não encontrado com o ID: " + id));
    }

    public Avaliado buscarPorSaram(String saram) {
        return repository.findBySaram(saram)
                .orElseThrow(() -> new RuntimeException("Avaliado não encontrado com o SARAM: " + saram));
    }

    public Avaliado atualizar(UUID id, Avaliado dados) {
        Avaliado existente = buscarPorId(id);

        if (dados.getSaram() != null && !dados.getSaram().equals(existente.getSaram())) {
            repository.findBySaram(dados.getSaram()).ifPresent(a -> {
                throw new IllegalArgumentException("SARAM já pertence a outro militar cadastrado.");
            });
            existente.setSaram(dados.getSaram());
        }

        if (dados.getNguerra() != null) existente.setNguerra(dados.getNguerra());
        if (dados.getSecao() != null) existente.setSecao(dados.getSecao());
        if (dados.getCodposto() != null) existente.setCodposto(dados.getCodposto());
        if (dados.getPosto() != null) existente.setPosto(dados.getPosto());
        if (dados.getQuadro() != null) existente.setQuadro(dados.getQuadro());
        if (dados.getFuncao() != null) existente.setFuncao(dados.getFuncao());
        if (dados.getAntiguidade() != null) existente.setAntiguidade(dados.getAntiguidade());
        if (dados.getAtivo() != null) existente.setAtivo(dados.getAtivo());

        return repository.save(existente);
    }

    public void deletar(UUID id) {
        Avaliado existente = buscarPorId(id);
        repository.delete(existente);
    }
}
