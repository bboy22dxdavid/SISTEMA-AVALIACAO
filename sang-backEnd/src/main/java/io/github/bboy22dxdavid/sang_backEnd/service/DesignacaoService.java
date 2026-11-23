package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.dto.DesignacaoRequest;
import io.github.bboy22dxdavid.sang_backEnd.model.DesignacaoSetor;
import io.github.bboy22dxdavid.sang_backEnd.model.Processo;
import io.github.bboy22dxdavid.sang_backEnd.model.StatusProcesso;
import io.github.bboy22dxdavid.sang_backEnd.model.User;
import io.github.bboy22dxdavid.sang_backEnd.repository.DesignacaoSetorRepository;
import io.github.bboy22dxdavid.sang_backEnd.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DesignacaoService {
    private final DesignacaoSetorRepository designacaoRepository;
    private final ProcessoService processoService;
    private final UserRepository userRepository;

    public DesignacaoService(DesignacaoSetorRepository designacaoRepository,
                             ProcessoService processoService,
                             UserRepository userRepository) {
        this.designacaoRepository = designacaoRepository;
        this.processoService = processoService;
        this.userRepository = userRepository;
    }

    public List<DesignacaoSetor> listarPorProcesso(UUID processoId) {
        return designacaoRepository.findByProcessoId(processoId);
    }

    public List<DesignacaoSetor> listarPorProcessoEAvaliador(UUID processoId, UUID avaliadorId) {
        return designacaoRepository.findByProcessoIdAndAvaliadorId(processoId, avaliadorId);
    }

    @Transactional
    public DesignacaoSetor designar(DesignacaoRequest request) {
        Processo processo = processoService.buscarPorId(request.processoId());

        if (processo.getStatus() == StatusProcesso.ENCERRADO) {
            throw new IllegalArgumentException("Não é possível alterar designações de um processo já ENCERRADO.");
        }

        User avaliador = userRepository.findById(request.avaliadorId())
                .orElseThrow(() -> new EntityNotFoundException("Avaliador não encontrado."));

        if (Boolean.FALSE.equals(avaliador.getAtivo())) {
            throw new IllegalArgumentException("O militar selecionado está inativo no sistema.");
        }

        if (!"ROLE_EVALUATOR".equalsIgnoreCase(avaliador.getRole().getName())) {
            throw new IllegalArgumentException("Apenas usuários com perfil ROLE_EVALUATOR podem ser designados para avaliar um setor.");
        }

        String secaoNormalizada = request.secao().trim().toUpperCase();

        // Se já existir designação para esta seção neste processo, atualiza o responsável
        DesignacaoSetor designacao = designacaoRepository
                .findByProcessoIdAndSecao(processo.getId(), secaoNormalizada)
                .orElseGet(DesignacaoSetor::new);

        designacao.setProcesso(processo);
        designacao.setSecao(secaoNormalizada);
        designacao.setAvaliador(avaliador);

        return designacaoRepository.save(designacao);
    }
}
