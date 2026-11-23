package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.model.Efetivo;
import io.github.bboy22dxdavid.sang_backEnd.model.User;
import org.springframework.stereotype.Component;

@Component
public class HierarquiaValidator {
    public void validarPrecedencia(User avaliador, Efetivo avaliado) {
        if (avaliador.getSaram().equals(avaliado.getSaram())) {
            throw new IllegalArgumentException("Regra Hierárquica: Um militar não pode avaliar a si mesmo.");
        }

        if (avaliador.getAntiguidade() >= avaliado.getAntiguidade()) {
            throw new IllegalArgumentException(
                    String.format("Bloqueio Hierárquico: %s (%s) não possui precedência sobre %s (%s).",
                            avaliador.getPosto(), avaliador.getUsername(),
                            avaliado.getPosto(), avaliado.getNomeGuerra())
            );
        }
    }
}
