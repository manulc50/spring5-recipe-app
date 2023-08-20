package com.mlorenzo.spring5recipeapp.services;

import java.util.Set;

import com.mlorenzo.spring5recipeapp.commands.RecipeCommand;

public interface RecipeService {
	Set<RecipeCommand> getRecipes();
	RecipeCommand findRecipeCommandById(Long id);
	RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);
	void deleteById(Long id);
}
