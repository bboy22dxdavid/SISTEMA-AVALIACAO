package io.github.bboy22dxdavid.sang_backEnd.adapter;

import io.github.bboy22dxdavid.sang_backEnd.model.Efetivo;
import io.github.bboy22dxdavid.sang_backEnd.model.TipoVinculo;
import io.github.bboy22dxdavid.sang_backEnd.repository.EfetivoProviderPort;
import io.github.bboy22dxdavid.sang_backEnd.repository.EfetivoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class DatabaseMockEfetivoAdapter implements EfetivoProviderPort {

    private final EfetivoRepository efetivoRepository;

    public DatabaseMockEfetivoAdapter(EfetivoRepository efetivoRepository) {
        this.efetivoRepository = efetivoRepository;
    }
    @Override
    public List<Efetivo> buscarElegiveis() {
        return efetivoRepository.findByTipoVinculoAndAtivoTrueOrderByAntiguidadeAsc(TipoVinculo.CARREIRA);
    }

    @Override
    public List<Efetivo> buscarElegiveisPorSecao(String secao) {
        return efetivoRepository.findBySecaoAndTipoVinculoAndAtivoTrueOrderByAntiguidadeAsc(secao, TipoVinculo.CARREIRA);
    }

    @Override
    public Optional<Efetivo> buscarPorId(UUID id) {
        return efetivoRepository.findById(id);
    }

    @Override
    public Optional<Efetivo> buscarPorSaram(String saram) {
        return efetivoRepository.findBySaram(saram);
    }
}
