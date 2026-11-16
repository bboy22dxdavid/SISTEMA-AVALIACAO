package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.model.Processo;
import io.github.bboy22dxdavid.sang_backEnd.repository.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/* =========================================================
 * class ProcessoService camada é responsável por
 * tratar os dados, validações e chamar o repositório
 * =========================================================
 * */

@Service
public class ProcessoService {

    //instancia da interface
    //@Autowired
    private final ProcessoRepository repository;

    // Injeção via construtor (Dispensa o @Autowired e facilita testes manuais)
    public ProcessoService(ProcessoRepository repository) {
        this.repository = repository;
    }

    /*======================
    *        CRUD
    * =====================*/
    //Metodo que salva
    public Processo salvar(Processo processo) {
        //valida se o ano de um processo e valido e garante que não seja anterior a o ano de 2000
        if (processo.getAno() == null || processo.getAno() < 2000) {
            throw new IllegalArgumentException("Ano do processo inválido!");
        } if (repository.findByAno(processo.getAno()).isPresent()) {
            throw new IllegalArgumentException("Já existe um processo cadastrado para o ano " + processo.getAno() + ".");
        }
        return repository.save(processo);
    }

    //metod que atualiza um registro
    public Processo atualizar(UUID id, Processo dadosAtualizados) {
        Processo existente = buscarPorId(id);
        if (dadosAtualizados.getAno() != null) {
            if (dadosAtualizados.getAno() < 2000) {
                throw new IllegalArgumentException("O ano do processo deve ser maior ou igual a 2000.");
            }
            repository.findByAno(dadosAtualizados.getAno())
                    .filter(p -> !p.getId().equals(id))
                    .ifPresent(p -> {
                        throw new IllegalArgumentException("Já existe outro processo cadastrado para o ano " + dadosAtualizados.getAno());
                    });
            existente.setAno(dadosAtualizados.getAno());
        }
        return repository.save(existente);
    }

    //Metodo que Lista todos
    public List<Processo> listarTodos(){
        return repository.findAll();
    }

    //Metodo que busca por Id
    public Processo buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Processo não encontrado com o ID: " + id));
    }

    //metodo que deleta
    public void deletar(UUID id) {
        Processo processo = buscarPorId(id);
        repository.delete(processo);
    }
}
