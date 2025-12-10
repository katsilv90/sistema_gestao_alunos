package com.cencal.gestaoalunos.controller;

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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ucs")
public class UnidadeCurricularController {

    private final UnidadeCurricularService unidadeCurricularService;
    private final CursoService cursoService;
    private final AlunoService alunoService;

    public UnidadeCurricularController(UnidadeCurricularService unidadeCurricularService,
                                       CursoService cursoService,
                                       AlunoService alunoService) {
        this.unidadeCurricularService = unidadeCurricularService;
        this.cursoService = cursoService;
        this.alunoService = alunoService;
    }

    @GetMapping
    public String mostrarUCs(Model model) {
        List<UnidadeCurricularModel> ucs = unidadeCurricularService.listarTodos();
        List<CursoModel> cursos = cursoService.listarTodos();

        model.addAttribute("ucs", ucs);
        model.addAttribute("cursos", cursos);
        model.addAttribute("ucModel", new UnidadeCurricularModel());
        model.addAttribute("pesquisaUc", new UnidadeCurricularModel());
        model.addAttribute("resultadosPesquisa", null);
        return "ucs";
    }

    @PostMapping
    public String adicionarUC(@RequestParam("cursoId") Long cursoId,
                              @ModelAttribute UnidadeCurricularModel ucModel,
                              RedirectAttributes redirectAttributes) {
        CursoModel curso = cursoService.buscarPorId(cursoId);
        if (curso == null) {
            redirectAttributes.addFlashAttribute("toastMensagem", "Curso não encontrado!");
            redirectAttributes.addFlashAttribute("toastTipo", "error");
            return "redirect:/ucs";
        }

        try {
            UnidadeCurricularModel ucSalva = unidadeCurricularService.salvar(ucModel);
            // Associa UC ao curso selecionado
            curso.getUcs().add(ucSalva);
            cursoService.salvar(curso);

            redirectAttributes.addFlashAttribute("toastMensagem", "UC adicionada ao curso com sucesso!");
            redirectAttributes.addFlashAttribute("toastTipo", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("toastMensagem", e.getMessage());
            redirectAttributes.addFlashAttribute("toastTipo", "error");
        }

        return "redirect:/ucs";
    }

    @GetMapping("/remover")
    public String removerUC(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        UnidadeCurricularModel uc = unidadeCurricularService.buscarPorId(id);

        if (uc == null) {
            redirectAttributes.addFlashAttribute("toastMensagem", "UC não encontrada!");
            redirectAttributes.addFlashAttribute("toastTipo", "error");
            return "redirect:/ucs";
        }

        // Verifica se algum aluno está inscrito nesta UC
        boolean possuiAlunos = alunoService.listarTodos()
                .stream()
                .anyMatch(a -> a.getUcs().contains(uc));

        if (possuiAlunos) {
            redirectAttributes.addFlashAttribute("toastMensagem",
                    "Não é possível remover a UC, existem alunos inscritos!");
            redirectAttributes.addFlashAttribute("toastTipo", "error");
            return "redirect:/ucs";
        }

        unidadeCurricularService.excluir(id);
        redirectAttributes.addFlashAttribute("toastMensagem", "UC removida com sucesso!");
        redirectAttributes.addFlashAttribute("toastTipo", "success");

        return "redirect:/ucs";
    }

    @GetMapping("/pesquisar")
    public String pesquisarUCs(@RequestParam(value = "nome", required = false) String nome, Model model) {
        List<UnidadeCurricularModel> todasUcs = unidadeCurricularService.listarTodos();
        List<CursoModel> cursos = cursoService.listarTodos();

        List<UnidadeCurricularModel> resultadosPesquisa = null;
        if (nome != null && !nome.trim().isEmpty()) {
            resultadosPesquisa = unidadeCurricularService.pesquisarPorNome(nome);
        }

        model.addAttribute("ucs", todasUcs);
        model.addAttribute("cursos", cursos);
        model.addAttribute("resultadosPesquisa", resultadosPesquisa);
        model.addAttribute("ucModel", new UnidadeCurricularModel());
        model.addAttribute("pesquisaUc", new UnidadeCurricularModel());
        return "ucs";
    }
}
