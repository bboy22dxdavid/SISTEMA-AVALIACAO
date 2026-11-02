package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;

/* ===============================================
* Classe que representa a tabela no banco Processo
* ===============================================
* */

@Entity
@Table(name= "tb_processos")
@Data
public class Processo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private Integer ano;

    // Construtor Padrão (Obrigatório para o Hibernate)
    public Processo() {}


    // Construtor Completo
    public Processo(String id, Integer ano) {
        this.id = id;
        this.ano = ano;
    }
}
