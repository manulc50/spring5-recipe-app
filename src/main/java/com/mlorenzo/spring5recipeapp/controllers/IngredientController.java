package com.mlorenzo.spring5recipeapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mlorenzo.spring5recipeapp.commands.IngredientCommand;
import com.mlorenzo.spring5recipeapp.services.IngredientService;
import com.mlorenzo.spring5recipeapp.services.UnitOfMeasureService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IngredientController {
	private final IngredientService ingredientService;
	private final UnitOfMeasureService unitOfMeasureService;	
	
	//@RequestMapping("/recipe/{recipeId}/ingredients") // Si no se indica el atributo "method" en esta anotación con el método de la petición http, por defecto es GET
	@GetMapping("/recipe/{recipeId}/ingredients") // Versión simplificada de la anotación @RequestMapping(value = "/recipe/{recipeId}/ingredients",method = RequestMethod.GET)
	public String listIngredients(@PathVariable(value = "recipeId") Long id, Model model) {
		log.debug("Getting ingredient list for recipe with id: " + id);
		model.addAttribute("recipeId", id);
		model.addAttribute("ingredients", ingredientService.getIngredients(id));
		return "recipe/ingredient/list";
	}
	
	//@RequestMapping(value = "recipe/{recipeId}/ingredient/{id}/show",method = RequestMethod.GET) // Si no se indica el atributo "method" en esta anotación con el método de la petición http, por defecto es GET
    @GetMapping("recipe/{recipeId}/ingredient/{id}/show") // Versión simplificada de la anotación @RequestMapping(value = "recipe/{recipeId}/ingredient/{id}/show",method = RequestMethod.GET)
	public String showRecipeIngredient(@PathVariable Long recipeId, @PathVariable(name = "id") Long ingredientId, Model model){
    	model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        return "recipe/ingredient/show";
    }
	
	//@RequestMapping("recipe/{recipeId}/ingredient/new") // Si no se indica el atributo "method" en esta anotación con el método de la petición http, por defecto es GET
    @GetMapping("recipe/{recipeId}/ingredient/new") // Versión simplificada de la anotación @RequestMapping(value = "recipe/{recipeId}/ingredient/new",method = RequestMethod.GET)
    public String newRecipeIngredient(@PathVariable Long recipeId, Model model){
        model.addAttribute("ingredient", ingredientService.createNewIngredient(recipeId));
        model.addAttribute("uomList",  unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientForm";
    }

	
    //@RequestMapping("recipe/{recipeId}/ingredient/{idIngredient}/update") // Si no se indica el atributo "method" en esta anotación con el método de la petición http, por defecto es GET
    @GetMapping("recipe/{recipeId}/ingredient/{idIngredient}/update") // Versión simplificada de la anotación @RequestMapping(value = "recipe/{recipeId}/ingredient/{idIngredient}/update",method = RequestMethod.GET)
    public String updateRecipeIngredient(@PathVariable Long recipeId, @PathVariable Long idIngredient, Model model){
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, idIngredient));
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientForm";
    }

    // La anotación @ModelAttribute hace que Spring vincule los datos introducidos en el formulario por el usuario con el objeto "command" de tipo IngredientCommand que se le pasa como argumento de entrada a este metodo
    //@RequestMapping(value = "recipe/{recipeId}/ingredient",method = RequestMethod.POST)
    @PostMapping("recipe/{recipeId}/ingredient") // Versión simplificada de la anotación @RequestMapping(value = "recipe/{recipeId}/ingredient",method = RequestMethod.POST)
    public String saveOrUpdate(@ModelAttribute IngredientCommand command){
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);
        log.debug("saved receipe id:" + savedCommand.getRecipeId());
        log.debug("saved ingredient id:" + savedCommand.getId());
        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }
    
    //@RequestMapping("recipe/{recipeId}/ingredient/{idIngredient}/delete") // Si no se indica el atributo "method" en esta anotación con el método de la petición http, por defecto es GET
    @GetMapping("recipe/{recipeId}/ingredient/{idIngredient}/delete") // Versión simplificada de la anotación @RequestMapping(value = "recipe/{recipeId}/ingredient/{idIngredient}/delete",method = RequestMethod.GET)
    public String deleteRecipeIngredient(@PathVariable Long recipeId, @PathVariable Long idIngredient){
    	log.debug("Deleting ingredient with id: " + idIngredient + " for recipe with id: " + recipeId);
    	ingredientService.deleteById(recipeId, idIngredient);
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }

}
