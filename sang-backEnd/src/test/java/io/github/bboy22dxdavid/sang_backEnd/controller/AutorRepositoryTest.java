package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.model.*;
import io.github.bboy22dxdavid.sang_backEnd.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository repository;//injetor o repository

    //executa os metodos salvar
    @Test
    public void salvarTest(){
        Autor autor = new Autor();
        autor.setNome("Maria");
        autor.setNacionalidade("Brasilerio");
        autor.setData_nascimento(LocalDate.of(1960, 1, 31));

        var autorSalvo = repository.save(autor);

        System.out.println("Autor Salvo: " + autorSalvo);

    }

    //executa os metodos atualizar
    @Test
    public void atualizar(){
        var id = UUID.fromString("939049b9-b43b-4ba0-bb61-9d46fb1b3710");

        Optional<Autor> possivelAutor = repository.findById(id);

        //verificando se o id exist ou
        if (possivelAutor.isPresent()){
            //obtendo o id encontrado e passando para uma variavel
            Autor autorEncontrado = possivelAutor.get();

            System.out.println("Dados do Autor:");
            System.out.println(autorEncontrado);

            //modificando os dados pelo id encontrado
            autorEncontrado.setNome("Maria Do Carmo");
           // autorEncontrado.setData_nascimento(LocalDate.of(1990, 4, 12));

            //atualizando os dados
            repository.save(autorEncontrado);
        }else{
            System.out.println("Dados do Autor não encontrados:");
        }
    }

    @Test
    public void listarTest(){
        //criando a lista de autrores
        List<Autor> lista = repository.findAll();

        //criando laco para percorrer toda a lista e capturando os dados
        lista.forEach(System.out::println);

    }

    @Autowired
    UserRepository userRepository;

    @Test
    public  void listarUser(){
        try {
            List<User> listaUser = userRepository.findAll();
            System.out.println("============================");
            listaUser.forEach(System.out::println);
            System.out.println("============================");
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Autowired
    RoleRepository rolerRepository;

    @Test
    public  void listarRoler(){

        try {
            List<Role> listaUser = rolerRepository.findAll();
            System.out.println("============================");
            listaUser.forEach(System.out::println);
            System.out.println("============================");
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Autowired
    ProcessoRepository processoRepository;

    @Test
    public  void listarProcesso(){

        try {
            List<Processo> listaUser = processoRepository.findAll();
            System.out.println("============================");
            listaUser.forEach(System.out::println);
            System.out.println("============================");
        } catch (Exception e){
            System.out.println(e);
        }
    }

    //erro, não esta retornando os valoresr
    @Autowired
    NivelamentoRepository NivelamentoRepository;

    @Test
    public void listarNivelamento(){

        try {
            List<Nivelamento> listaUser = NivelamentoRepository.findAll();
            System.out.println("============================");
            listaUser.forEach(System.out::println);
            System.out.println("============================");
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Autowired
    AvaliadoRepository AvaliadoRepository;

    @Test
    public void listarAvaliado(){

        try {
            List<Avaliado> listaUser = AvaliadoRepository.findAll();
            System.out.println("============================");
            listaUser.forEach(System.out::println);
            System.out.println("============================");
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Autowired
    AvaliacaoRepository avaliacaoRepository;

    @Test
    public void listarAvaliacao(){

        try {
            List<Avaliacao> listaUser = avaliacaoRepository.findAll();
            System.out.println("============================");
            listaUser.forEach(System.out::println);
            System.out.println("============================");
        } catch (Exception e){
            System.out.println(e);
        }
    }

    //executa os metodo deletando pelo objeto
    @Test
    public void deletarObjTest(){
        //obter o id
        var id = UUID.fromString("75c6c93b-b453-42bb-b740-e2b574ab419b");
        var teste = repository.findById(id).get();

        //chamando o metodo delete
        repository.delete(teste);
    }
}
