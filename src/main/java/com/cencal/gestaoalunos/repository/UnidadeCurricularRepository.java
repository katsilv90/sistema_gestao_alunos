package com.cencal.gestaoalunos.repository;

import com.cencal.gestaoalunos.model.UnidadeCurricularModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UnidadeCurricularRepository extends JpaRepository<UnidadeCurricularModel, Long> {
    List<UnidadeCurricularModel> findByNome(String nome);
    List<UnidadeCurricularModel> findByNomeContaining(String parte);
    List<UnidadeCurricularModel> findByNomeStartingWith(String prefixo);
    List<UnidadeCurricularModel> findByNomeContainingIgnoreCase(String nome);

    List<UnidadeCurricularModel> findAll();
}
