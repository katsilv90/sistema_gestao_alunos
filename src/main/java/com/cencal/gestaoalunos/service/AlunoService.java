package com.cencal.gestaoalunos.service;

import com.cencal.gestaoalunos.model.AlunoModel;
import com.cencal.gestaoalunos.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public List<AlunoModel> listarTodos(){
        return alunoRepository.findAll();
    }

    public AlunoModel buscarPorId(Long id) {
        return alunoRepository.findById(id).orElse(null);
    }

    public AlunoModel salvar(AlunoModel alunoModel){
        if (alunoModel.getNome().isBlank())
            throw new IllegalArgumentException("O nome é obrigatório!");
        return alunoRepository.save(alunoModel);
    }

    public void excluir(Long id){
        alunoRepository.deleteById(id);
    }

    public List<AlunoModel> pesquisarPorNome(String nome) {
        return alunoRepository.findByNomeContainingIgnoreCase(nome);
    }

    //emails
    public boolean emailExiste(String email) {
        return alunoRepository.existsByEmail(email);
    }

}
