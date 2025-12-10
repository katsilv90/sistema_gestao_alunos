package com.cencal.gestaoalunos.repository;

import com.cencal.gestaoalunos.model.AlunoModel;
import com.cencal.gestaoalunos.model.CursoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlunoRepository extends JpaRepository<AlunoModel, Long>{
    List<AlunoModel> findByNome(String nome);
    List<AlunoModel> findByNomeContaining(String parte);
    List<AlunoModel> findByNomeStartingWith(String prefixo);
    List<AlunoModel> findByNomeContainingIgnoreCase(String nome);

    List<AlunoModel> findAll();

    boolean existsByEmail(String email);
    boolean existsByUcs_Id(Long ucId);

}
