package com.mlorenzo.spring5recipeapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mlorenzo.spring5recipeapp.services.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {
	private final RecipeService recipeService;
	
	//@RequestMapping({"","/","/index"}) // Si no se especifica en esta anotación otro tipo de método para las peticiones http asociadas a estar urls, por defecto se usa el método GET
	@GetMapping({"","/","/index"}) // Versión simplificada de la anotación @RequestMapping(value = {"","/","/index"}, method = RequestMethod.GET)
	public String getIndexPage(Model model) {
		log.debug("Getting Index page");
		model.addAttribute("recipes", recipeService.getRecipes());
		return "index";
	}
}
