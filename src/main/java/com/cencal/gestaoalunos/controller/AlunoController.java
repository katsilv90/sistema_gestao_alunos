package com.cencal.gestaoalunos.controller;

import com.cencal.gestaoalunos.model.AlunoModel;
import com.cencal.gestaoalunos.model.CursoModel;
import com.cencal.gestaoalunos.model.UnidadeCurricularModel;
import com.cencal.gestaoalunos.service.AlunoService;
import com.cencal.gestaoalunos.service.CursoService;
import com.cencal.gestaoalunos.service.UnidadeCurricularService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;
    private final CursoService cursoService;
    private final UnidadeCurricularService unidadeCurricularService;

    public AlunoController(AlunoService alunoService, CursoService cursoService,
                           UnidadeCurricularService unidadeCurricularService) {
        this.alunoService = alunoService;
        this.cursoService = cursoService;
        this.unidadeCurricularService = unidadeCurricularService;
    }

    // GET /alunos
    @GetMapping
    public String mostrarAlunos(Model model) {
        model.addAttribute("alunoModel", new AlunoModel());
        model.addAttribute("alunos", alunoService.listarTodos());
        model.addAttribute("cursos", cursoService.listarTodos());
        model.addAttribute("ucs", unidadeCurricularService.listarTodos());
        return "alunos";
    }

    // POST /alunos
    @PostMapping
    public String adicionarAluno(
            @ModelAttribute AlunoModel alunoModel,
            @RequestParam("cursoId") Long cursoId,
            @RequestParam(value = "ucsIds", required = false) List<Long> ucsIds,
            RedirectAttributes redirectAttributes) {

        // Verificar email duplicado ANTES de salvar
        if (alunoService.emailExiste(alunoModel.getEmail())) {
            redirectAttributes.addFlashAttribute("toastMensagem", "Já existe um aluno registado com este email!");
            redirectAttributes.addFlashAttribute("toastTipo", "error");
            return "redirect:/alunos";
        }

        // Buscar e setar curso
        CursoModel curso = cursoService.buscarPorId(cursoId);
        if (curso == null) {
            redirectAttributes.addFlashAttribute("toastMensagem", "Curso não encontrado!");
            redirectAttributes.addFlashAttribute("toastTipo", "error");
            return "redirect:/alunos";
        }
        alunoModel.setCurso(curso);

        // Buscar e setar UCs selecionadas (se houver)
        if (ucsIds != null && !ucsIds.isEmpty()) {
            List<UnidadeCurricularModel> ucsSelecionadas = unidadeCurricularService
                    .listarTodos()
                    .stream()
                    .filter(uc -> ucsIds.contains(uc.getId()))
                    .toList();
            alunoModel.setUcs(ucsSelecionadas);
        }

        // Salvar aluno
        try {
            alunoService.salvar(alunoModel);
            redirectAttributes.addFlashAttribute("toastMensagem", "Aluno adicionado com sucesso!");
            redirectAttributes.addFlashAttribute("toastTipo", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("toastMensagem", e.getMessage());
            redirectAttributes.addFlashAttribute("toastTipo", "error");
        }

        return "redirect:/alunos";
    }

    // GET /alunos/remover
    @GetMapping("/remover")
    public String removerAluno(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        AlunoModel aluno = alunoService.buscarPorId(id);
        if (aluno != null) {
            alunoService.excluir(id);
            redirectAttributes.addFlashAttribute("toastMensagem", "Aluno removido com sucesso!");
            redirectAttributes.addFlashAttribute("toastTipo", "success");
        } else {
            redirectAttributes.addFlashAttribute("toastMensagem", "Aluno não encontrado!");
            redirectAttributes.addFlashAttribute("toastTipo", "error");
        }
        return "redirect:/alunos";
    }

    // GET /alunos/pesquisar
    @GetMapping("/pesquisar")
    public String pesquisarAlunos(@ModelAttribute("alunoModel") AlunoModel alunoModel, Model model) {
        model.addAttribute("alunoModel", new AlunoModel());
        model.addAttribute("alunos", alunoService.pesquisarPorNome(alunoModel.getNome()));
        model.addAttribute("cursos", cursoService.listarTodos());
        model.addAttribute("ucs", unidadeCurricularService.listarTodos());
        return "alunos";
    }

    //GET/ucs por curso
    @GetMapping("/ucs-por-curso")
    @ResponseBody
    public List<UnidadeCurricularModel> listarUCsPorCurso(@RequestParam("cursoId") Long cursoId) {
        CursoModel curso = cursoService.buscarPorId(cursoId);
        if (curso != null) {
            return curso.getUcs();
        }
        return List.of();
    }
}
