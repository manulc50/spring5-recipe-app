package com.mlorenzo.spring5recipeapp.services;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mlorenzo.spring5recipeapp.commands.IngredientCommand;
import com.mlorenzo.spring5recipeapp.commands.UnitOfMeasureCommand;
import com.mlorenzo.spring5recipeapp.converters.IngredientCommandToIngredient;
import com.mlorenzo.spring5recipeapp.converters.IngredientToIngredientCommand;
import com.mlorenzo.spring5recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.mlorenzo.spring5recipeapp.domain.Ingredient;
import com.mlorenzo.spring5recipeapp.domain.Recipe;
import com.mlorenzo.spring5recipeapp.exceptions.NotFoundException;
import com.mlorenzo.spring5recipeapp.repositories.RecipeRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class IngredientServiceImpl implements IngredientService {
	private final RecipeRepository recipeRepository;
	private final IngredientToIngredientCommand ingrToIngrCommandConverter;
	private final IngredientCommandToIngredient ingrCommandToIngrConverter;
	private final UnitOfMeasureCommandToUnitOfMeasure uomCommandToUomConverter;

	@Override
	public Set<IngredientCommand> getIngredients(Long recipeId) {
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + recipeId));
		return recipe.getIngredients().stream()
				// Versión simplificada de la expresión "ingredient -> ingrToIngrCommandConverter.convert(ingredient)"
				.map(ingrToIngrCommandConverter::convert)
				.collect(Collectors.toSet());
	}
	
	@Override
	public IngredientCommand createNewIngredient(Long recipeId) {
		Recipe recipe = recipeRepository.findById(recipeId)
			.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + recipeId));
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipe.getId());
        ingredientCommand.setUom(new UnitOfMeasureCommand());
        return ingredientCommand;
	}

	@Override
	public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + recipeId));
        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst();
        if(!ingredientOptional.isPresent()) {
            log.error("Ingredient id not found: " + ingredientId);
            throw new NotFoundException("Ingredient Not Found for id value: " + ingredientId);
        }
        return ingrToIngrCommandConverter.convert(ingredientOptional.get());
	}

	@Override
	public IngredientCommand saveIngredientCommand(IngredientCommand command) {
		Recipe recipe = recipeRepository.findById(command.getRecipeId())
				.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + command.getRecipeId()));
        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();
        //update existing ingredient
        if(ingredientOptional.isPresent()){
            Ingredient ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(command.getDescription());
            ingredientFound.setAmount(command.getAmount());
            ingredientFound.setUom(uomCommandToUomConverter.convert(command.getUom()));
        }
        //add new ingredient
        else 
        	recipe.addIngredient(ingrCommandToIngrConverter.convert(command));
        Recipe savedRecipe = recipeRepository.save(recipe);
        // case of updating an ingredient
        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                .filter(recipeIngredient -> recipeIngredient.getId().equals(command.getId()))
                .findFirst();
        // case of creating a new ingredient - check by description,amount and uom
        if(!savedIngredientOptional.isPresent()) {
            //not totally safe... But best guess
            savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                    .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                    .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUom().getId()))
                    .findFirst();
        }
        return ingrToIngrCommandConverter.convert(savedIngredientOptional.get());
    }

	@Override
	public void deleteById(Long recipeId, Long ingredientId) {
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + recipeId));
        Optional<Ingredient> removedIngredientOptional = recipe.getIngredients().stream()
        		.filter(ingredient -> ingredient.getId().equals(ingredientId))
        		.findFirst();
        if (removedIngredientOptional.isPresent()) {
        	Ingredient removedIngredient = removedIngredientOptional.get();
            removedIngredient.setRecipe(null); // Al poner nula la receta, anula la relación entre el ingrediente y la receta y esto hará que Hibernate elimine el ingrediente de la base de datos
            recipeRepository.save(recipe);
        }
	}
}
