package com.cencal.gestaoalunos.service;

import com.cencal.gestaoalunos.model.CursoModel;
import com.cencal.gestaoalunos.model.UnidadeCurricularModel;
import com.cencal.gestaoalunos.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {
    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<CursoModel> listarTodos(){
        return cursoRepository.findAll();
    }

    public CursoModel buscarPorId(Long id){
        return cursoRepository.findById(id).orElse(null);
    }


    public CursoModel salvar(CursoModel cursoModel){
        if (cursoModel.getNome().isBlank())
            throw new IllegalArgumentException("Curso obrigatório!");
        return cursoRepository.save(cursoModel);
    }

    public void excluir(Long id){
        cursoRepository.deleteById(id);
    }

    public List<CursoModel> pesquisarPorNome(String nome) {
        return cursoRepository.findByNomeContainingIgnoreCase(nome);
    }
}
