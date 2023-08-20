package com.mlorenzo.spring5recipeapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mlorenzo.spring5recipeapp.commands.IngredientCommand;
import com.mlorenzo.spring5recipeapp.converters.IngredientCommandToIngredient;
import com.mlorenzo.spring5recipeapp.converters.IngredientToIngredientCommand;
import com.mlorenzo.spring5recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.mlorenzo.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.mlorenzo.spring5recipeapp.domain.Ingredient;
import com.mlorenzo.spring5recipeapp.domain.Recipe;
import com.mlorenzo.spring5recipeapp.repositories.RecipeRepository;
import com.mlorenzo.spring5recipeapp.repositories.UnitOfMeasureRepository;

public class IngredientServiceImplTest {
	
	@Mock
    RecipeRepository recipeRepository;
	
	@Mock
	UnitOfMeasureRepository unitOfMeasureRepository;
	
	IngredientToIngredientCommand ingredientToIngredientCommand;
	IngredientCommandToIngredient ingredientCommandToIngredient;
	IngredientService ingredientService;
	
	//init converters
    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        this.ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }
    
    @BeforeEach
    void setUp() throws Exception {
    	// Para poder usar Mockito en esta clase de pruebas
        MockitoAnnotations.initMocks(this); // Otra opción a esta línea es anotar la clase con @ExtendWith(MockitoExtension.class)
        ingredientService = new IngredientServiceImpl(recipeRepository,unitOfMeasureRepository,ingredientToIngredientCommand,ingredientCommandToIngredient);
    }
    
    @Test
    void findByRecipeIdAndReceipeIdHappyPathTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(1L);
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        Optional<Recipe> recipeOptional = Optional.of(recipe);
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L, 3L);
        assertEquals(Long.valueOf(3L), ingredientCommand.getId());
        assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeRepository, times(1)).findById(anyLong());
    }
    
    @Test
    void saveRecipeCommandTest() throws Exception {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId(3L);
        command.setRecipeId(2L);
        Optional<Recipe> recipeOptional = Optional.of(new Recipe());
        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId(3L);
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);
        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);
        //then
        assertEquals(Long.valueOf(3L), savedCommand.getId());
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
    
    @Test
    void deleteByIdTest() throws Exception {
    	//given
        Long idToDelete = Long.valueOf(2L);
        Long idRecipe = 3L;
        Ingredient ingredient = new Ingredient();
        ingredient.setId(idToDelete);
        Recipe recipe = new Recipe();
        recipe.getIngredients().add(ingredient);
        ingredient.setRecipe(recipe);
        Optional<Recipe> recipeOptional = Optional.of(recipe);
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        //when
        ingredientService.deleteById(idRecipe, idToDelete);
        //then
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository).save(any(Recipe.class));
    }
}
