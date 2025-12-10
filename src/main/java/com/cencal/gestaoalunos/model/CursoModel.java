package com.cencal.gestaoalunos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "curso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    // 1 Curso -> Muitos Alunos
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlunoModel> alunos = new ArrayList<>();

    // N:M Curso <-> UCs
    @ManyToMany
    @JoinTable(
            name = "curso_uc",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "uc_id")
    )
    private List<UnidadeCurricularModel> ucs = new ArrayList<>();
}
