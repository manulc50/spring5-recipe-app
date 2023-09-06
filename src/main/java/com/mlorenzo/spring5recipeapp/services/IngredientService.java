package com.mlorenzo.spring5recipeapp.services;

import java.util.Set;

import com.mlorenzo.spring5recipeapp.commands.IngredientCommand;

public interface IngredientService {
	Set<IngredientCommand> getIngredients(Long recipeId);
	IngredientCommand createNewIngredient(Long recipeId);
	IngredientCommand findByRecipeIdAndIngredientId(Long recipeId,Long ingredientId);
	IngredientCommand saveIngredientCommand(IngredientCommand command);
	void deleteById(Long recipeId, Long ingredientId);
}
