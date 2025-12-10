package com.cencal.gestaoalunos.controller;

import com.cencal.gestaoalunos.model.CursoModel;
import com.cencal.gestaoalunos.service.CursoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    // Página principal de cursos
    @GetMapping
    public String mostrarCursos(Model model) {
        List<CursoModel> cursos = cursoService.listarTodos();
        model.addAttribute("cursos", cursos);
        model.addAttribute("cursoModel", new CursoModel());
        model.addAttribute("pesquisaCurso", new CursoModel());
        model.addAttribute("resultadosPesquisa", null); // ← IMPORTANTE
        return "cursos";
    }

    // Adicionar curso
    @PostMapping
    public String adicionarCurso(@ModelAttribute CursoModel cursoModel, RedirectAttributes redirectAttributes) {
        cursoService.salvar(cursoModel);
        redirectAttributes.addFlashAttribute("toastMensagem", "Curso adicionado com sucesso!");
        redirectAttributes.addFlashAttribute("toastTipo", "success");
        return "redirect:/cursos";
    }

    // Remover curso
    @GetMapping("/remover")
    public String removerCurso(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        CursoModel curso = cursoService.buscarPorId(id);
        if (curso != null) {
            // Verificar se há alunos inscritos
            if (!curso.getAlunos().isEmpty()) {
                redirectAttributes.addFlashAttribute("toastMensagem",
                        "Não é possível remover este curso: existem alunos inscritos!");
                redirectAttributes.addFlashAttribute("toastTipo", "error");
                return "redirect:/cursos";
            }

            cursoService.excluir(id);
            redirectAttributes.addFlashAttribute("toastMensagem", "Curso removido com sucesso!");
            redirectAttributes.addFlashAttribute("toastTipo", "success");
        } else {
            redirectAttributes.addFlashAttribute("toastMensagem", "Curso não encontrado!");
            redirectAttributes.addFlashAttribute("toastTipo", "error");
        }
        return "redirect:/cursos";
    }

    @GetMapping("/pesquisar")
    public String pesquisarCursos(@RequestParam(value = "nome", required = false) String nome, Model model) {
        List<CursoModel> resultadosPesquisa = null;
        List<CursoModel> todosCursos = cursoService.listarTodos(); // ← Mantém lista completa

        if (nome != null && !nome.trim().isEmpty()) {
            resultadosPesquisa = cursoService.pesquisarPorNome(nome);
        }

        model.addAttribute("cursos", todosCursos); // ← Sempre mostra todos os cursos
        model.addAttribute("resultadosPesquisa", resultadosPesquisa); // ← Só resultados da pesquisa
        model.addAttribute("cursoModel", new CursoModel());
        model.addAttribute("pesquisaCurso", new CursoModel());
        return "cursos";
    }
}
