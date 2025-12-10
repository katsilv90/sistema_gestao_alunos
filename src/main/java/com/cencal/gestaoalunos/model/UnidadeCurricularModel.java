package com.cencal.gestaoalunos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "unidade_curricular")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeCurricularModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    // mappedBy para "ucs" (nome do atributo em CursoModel)
    @ManyToMany(mappedBy = "ucs")
    @JsonIgnore // <- Adicionado
    private List<CursoModel> cursos = new ArrayList<>();
}
