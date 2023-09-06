package com.mlorenzo.spring5recipeapp.controllers;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mlorenzo.spring5recipeapp.commands.CategoryCommand;
import com.mlorenzo.spring5recipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5recipeapp.services.CategoryService;
import com.mlorenzo.spring5recipeapp.services.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RecipeController {
	private final RecipeService recipeService;
	private final CategoryService categoryService;
	
	//@RequestMapping(value = "/recipe/{recipeId}/show", method = RequestMethod.GET ) // Si no se indica el atributo "method" en esta anotación con el método de la petición http, por defecto es GET
	@GetMapping("/recipe/{recipeId}/show") // Versión simplificada de la anotación @RequestMapping(value = "/recipe/{recipeId}/show", method = RequestMethod.GET)
	public String showById(@PathVariable(name = "recipeId") Long id, Model model) {
		model.addAttribute("recipe", recipeService.findRecipeCommandById(id));
		return "recipe/show";
	}
	
	// El atributo de modelo "categories" es global porque se usa en las vistas devueltas por los métodos handler "newRecipe" y "updateRecipe"
	// Y, además, se obtiene en el método handler "saveOrUpdate" para obtener las nuevas categorías para la receta seleccionadas por el usuario a través del formulario
	@ModelAttribute("categorySet")
	public Set<CategoryCommand> getAllCategories(){
		return categoryService.getCategories();
	}
	
	//@RequestMapping("/recipe/new") // Si no se indica el atributo "method" en esta anotación con el método de la petición http, por defecto es GET
	@GetMapping("/recipe/new") // Versión simplificada de la anotación @RequestMapping(value = "/recipe/new", method = RequestMethod.GET)
	public String newRecipe(Model model) {
		model.addAttribute("recipe", new RecipeCommand());
		return "recipe/recipeForm";
	}
	
	//@RequestMapping("/recipe/{recipeId}/update") // Si no se indica el atributo "method" en esta anotación con el método de la petición http, por defecto es GET
	@GetMapping("/recipe/{recipeId}/update") // Versión simplificada de la anotación @RequestMapping(value = "/recipe/{recipeId}/update", method = RequestMethod.GET)
	public String updateRecipe(@PathVariable Long recipeId,Model model) {
		model.addAttribute("recipe", recipeService.findRecipeCommandById(recipeId));
		return "recipe/recipeForm";
	}
	
	// La anotación @ModelAttribute hace que Spring vincule los datos introducidos en el formulario por el usuario contenidos en el atributo "recipe" con el objeto "recipeCommand" de tipo RecipeCommand que se le pasa como argumento de entrada a este metodo
	//@RequestMapping(value = "/recipe", method = RequestMethod.POST)
	@PostMapping("/recipe") // Versión simplificada de la anotación @RequestMapping(value = "/recipe", method = RequestMethod.POST)
	public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand recipeCommand, BindingResult bindingResult) {
		if(bindingResult.hasErrors()){
			if(log.isDebugEnabled())
	            bindingResult.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
            return "recipe/recipeForm";
        }
		RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);
		return "redirect:/recipe/" + savedRecipeCommand.getId() + "/show";
	}
	
	//@RequestMapping("/recipe/{recipeId}/delete") // Si no se indica el atributo "method" en esta anotación con el método de la petición http, por defecto es GET
	@GetMapping("/recipe/{recipeId}/delete") // Versión simplificada de la anotación @RequestMapping(value = "/recipe/{recipeId}/delete", method = RequestMethod.GET)
	public String deleteRecipe(@PathVariable(value = "recipeId") Long id,Model model) {
		log.debug("Deleting recipe with id: " + id);
		recipeService.deleteById(id);
		return "redirect:/";
	}

}
