package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.dto.ProcessoRequest;
import io.github.bboy22dxdavid.sang_backEnd.model.Processo;
import io.github.bboy22dxdavid.sang_backEnd.model.StatusProcesso;
import io.github.bboy22dxdavid.sang_backEnd.repository.ProcessoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProcessoService {

    private final ProcessoRepository processoRepository;

    public ProcessoService(ProcessoRepository processoRepository) {
        this.processoRepository = processoRepository;
    }

    public List<Processo> listarTodos() {
        return processoRepository.findAllByOrderByAnoDesc();
    }

    public Processo buscarPorId(UUID id) {
        return processoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Processo anual não encontrado."));
    }

    @Transactional
    public Processo criar(ProcessoRequest request) {
        if (processoRepository.existsByAno(request.ano())) {
            throw new IllegalArgumentException("Já existe um processo cadastrado para o ano " + request.ano() + ".");
        }

        Processo processo = new Processo();
        processo.setAno(request.ano());
        processo.setStatus(StatusProcesso.ABERTO);

        return processoRepository.save(processo);
    }

    @Transactional
    public Processo encerrar(UUID id) {
        Processo processo = buscarPorId(id);
        processo.setStatus(StatusProcesso.ENCERRADO);
        return processoRepository.save(processo);
    }
}
