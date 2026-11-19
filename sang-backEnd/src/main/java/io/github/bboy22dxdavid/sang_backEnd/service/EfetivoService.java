package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.repository.EfetivoProviderPort;
import io.github.bboy22dxdavid.sang_backEnd.model.Efetivo;
import io.github.bboy22dxdavid.sang_backEnd.model.TipoVinculo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EfetivoService {
    private final EfetivoProviderPort efetivoProvider;

    public EfetivoService(EfetivoProviderPort efetivoProvider) {
        this.efetivoProvider = efetivoProvider;
    }

    public List<Efetivo> listarElegiveis() {
        return efetivoProvider.buscarElegiveis();
    }

    public List<Efetivo> listarElegiveisPorSecao(String secao) {
        return efetivoProvider.buscarElegiveisPorSecao(secao);
    }

    public Efetivo buscarElegivelPorId(UUID id) {
        Efetivo militar = efetivoProvider.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Militar não encontrado no efetivo."));

        validarElegibilidade(militar);
        return militar;
    }

    public Efetivo buscarElegivelPorSaram(String saram) {
        Efetivo militar = efetivoProvider.buscarPorSaram(saram)
                .orElseThrow(() -> new EntityNotFoundException("Militar não encontrado com o SARAM: " + saram));

        validarElegibilidade(militar);
        return militar;
    }

    public void validarElegibilidade(Efetivo militar) {
        if (Boolean.FALSE.equals(militar.getAtivo())) {
            throw new IllegalArgumentException("Militar inativo não é elegível para avaliação.");
        }
        if (militar.getTipoVinculo() != TipoVinculo.CARREIRA) {
            throw new IllegalArgumentException("Militares com vínculo TEMPORARIO não são elegíveis para o processo de avaliação.");
        }
    }

}
