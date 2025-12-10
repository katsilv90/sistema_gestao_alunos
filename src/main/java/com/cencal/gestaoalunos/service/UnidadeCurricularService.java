package com.cencal.gestaoalunos.service;

import com.cencal.gestaoalunos.model.UnidadeCurricularModel;
import com.cencal.gestaoalunos.repository.AlunoRepository;
import com.cencal.gestaoalunos.repository.UnidadeCurricularRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UnidadeCurricularService {

    private final UnidadeCurricularRepository unidadeCurricularRepository;
    private final AlunoRepository alunoRepository;

    public UnidadeCurricularService(UnidadeCurricularRepository unidadeCurricularRepository,
                                    AlunoRepository alunoRepository) {
        this.unidadeCurricularRepository = unidadeCurricularRepository;
        this.alunoRepository = alunoRepository;
    }

    public List<UnidadeCurricularModel> listarTodos() {
        return unidadeCurricularRepository.findAll();
    }

    public UnidadeCurricularModel buscarPorId(Long id) {
        return unidadeCurricularRepository.findById(id).orElse(null);
    }

    public UnidadeCurricularModel salvar(UnidadeCurricularModel unidadeCurricularModel) {
        if (unidadeCurricularModel.getNome() == null || unidadeCurricularModel.getNome().isBlank()) {
            throw new IllegalArgumentException("Unidade Curricular é obrigatória!");
        }
        return unidadeCurricularRepository.save(unidadeCurricularModel);
    }

    @Transactional
    public void excluir(Long ucId) {
        UnidadeCurricularModel uc = unidadeCurricularRepository.findById(ucId)
                .orElseThrow(() -> new RuntimeException("UC não encontrada"));

        // Verifica se existem alunos associados a esta UC
        boolean existeAluno = alunoRepository.existsByUcs_Id(ucId);
        if (existeAluno) {
            throw new RuntimeException("Não é possível remover esta UC: existem alunos associados.");
        }

        // Remove associações com cursos
        if (uc.getCursos() != null) {
            uc.getCursos().forEach(curso -> curso.getUcs().remove(uc));
            uc.getCursos().clear();
        }

        // Agora pode deletar a UC
        unidadeCurricularRepository.delete(uc);
    }

    public List<UnidadeCurricularModel> pesquisarPorNome(String nome) {
        return unidadeCurricularRepository.findByNomeContainingIgnoreCase(nome);
    }
}
