package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/* ===============================================
* Classe que representa a tabela no banco Processo
* ===============================================
* */

@Entity
@Table(name= "tb_processos")
@Data
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Integer ano;

    // Construtor Padrão (Obrigatório para o Hibernate)
    public Processo() {}


    // Construtor Completo

    public Processo(UUID id, Integer ano) {
        this.id = id;
        this.ano = ano;
    }
}
