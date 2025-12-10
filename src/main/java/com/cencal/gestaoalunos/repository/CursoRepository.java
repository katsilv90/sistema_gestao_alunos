package com.cencal.gestaoalunos.repository;

import com.cencal.gestaoalunos.model.AlunoModel;
import com.cencal.gestaoalunos.model.CursoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<CursoModel, Long> {
    List<CursoModel> findByNome(String nome);
    List<CursoModel> findByNomeContaining(String parte);
    List<CursoModel> findByNomeStartingWith(String prefixo);
    List<CursoModel> findByNomeContainingIgnoreCase(String nome);

    List<CursoModel> findAll();
}

