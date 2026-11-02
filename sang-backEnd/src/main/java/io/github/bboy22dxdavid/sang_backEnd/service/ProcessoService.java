package io.github.bboy22dxdavid.sang_backEnd.service;

import io.github.bboy22dxdavid.sang_backEnd.model.Processo;
import io.github.bboy22dxdavid.sang_backEnd.repository.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/* =========================================================
 * class ProcessoService camada é responsável por
 * tratar os dados, validações e chamar o repositório
 * =========================================================
 * */

@Service
public class ProcessoService {

    //instancia da interface
    @Autowired
    private ProcessoRepository repository;

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
        }
        return repository.save(processo);
    }

    //metod que atualiza um registro
    public Processo atualizar(Long id, Processo dadosAtualizados) {
        Processo processoExistente = buscarPorId(id);
        processoExistente.setAno(dadosAtualizados.getAno());
        return repository.save(processoExistente);
    }

    //Metodo que Lista todos
    public List<Processo> listarTodos(){
        return repository.findAll();
    }

    //Metodo que busca por Id
    public Processo buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Processo não encontrado com o ID: " + id));
    }

    //metodo que deleta
    public void deletar(Long id) {
        Processo processo = buscarPorId(id);
        repository.delete(processo);
    }
}
