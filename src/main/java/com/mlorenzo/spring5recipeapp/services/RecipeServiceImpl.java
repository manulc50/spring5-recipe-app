package com.mlorenzo.spring5recipeapp.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.mlorenzo.spring5recipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5recipeapp.converters.RecipeCommandToRecipe;
import com.mlorenzo.spring5recipeapp.converters.RecipeToRecipeCommand;
import com.mlorenzo.spring5recipeapp.domain.Recipe;
import com.mlorenzo.spring5recipeapp.exceptions.NotFoundException;
import com.mlorenzo.spring5recipeapp.repositories.RecipeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService{
	private final RecipeRepository recipeRepository;
	private final RecipeCommandToRecipe recipeCommandToRecipe;
	private final RecipeToRecipeCommand recipeToRecipeCommand;
	
	public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
		this.recipeRepository = recipeRepository;
		this.recipeCommandToRecipe = recipeCommandToRecipe;
		this.recipeToRecipeCommand =recipeToRecipeCommand;
	}

	@Override
	public Set<RecipeCommand> getRecipes() {
		log.debug("I'm in the Recipe Service");
		Set<RecipeCommand> recipeCommands = new HashSet<RecipeCommand>();
		recipeRepository.findAll().forEach(recipe -> recipeCommands.add(recipeToRecipeCommand.convert(recipe)));
		return recipeCommands;
	}

	
	@Override
	public RecipeCommand findRecipeCommandById(Long id) {
		Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + id));
		return recipeToRecipeCommand.convert(recipe);
	}

	@Override
	public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
		Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);
		Recipe savedRecipe = recipeRepository.save(detachedRecipe);
		log.debug("Saved Recipe Id: " + savedRecipe.getId());
		return recipeToRecipeCommand.convert(savedRecipe);
	}

	@Override
	public void deleteById(Long id) {
		recipeRepository.deleteById(id);	
	}

}
