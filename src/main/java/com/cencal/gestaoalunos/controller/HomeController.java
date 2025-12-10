package com.cencal.gestaoalunos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home"; // Agora retorna diretamente a página home
    }

    @GetMapping("/home")
    public String homePage() {
        return "home"; // Opcional: também responde a /home
    }
}