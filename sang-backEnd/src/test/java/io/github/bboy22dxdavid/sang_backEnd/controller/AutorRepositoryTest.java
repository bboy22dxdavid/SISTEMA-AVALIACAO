package io.github.bboy22dxdavid.sang_backEnd.controller;

import io.github.bboy22dxdavid.sang_backEnd.model.Autor;
import io.github.bboy22dxdavid.sang_backEnd.repository.AutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
            autorEncontrado.setData_nascimento(LocalDate.of(1990, 4, 12));

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

    //executa os metodo deletando pelo objeto
    @Test
    public void deletarObjTest(){
        //obter o id
        var id = UUID.fromString("939049b9-b43b-4ba0-bb61-9d46fb1b3710");
        var teste = repository.findById(id).get();

        //chamando o metodo delete
        repository.delete(teste);
    }
}
