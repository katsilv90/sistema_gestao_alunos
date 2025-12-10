package com.cencal.gestaoalunos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alunos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private Integer idade;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private CursoModel curso;

    @ManyToMany
    @JoinTable(
            name = "aluno_uc",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "uc_id")
    )
    private List<UnidadeCurricularModel> ucs = new ArrayList<>();
}