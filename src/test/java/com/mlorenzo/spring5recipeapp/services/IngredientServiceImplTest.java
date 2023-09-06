package com.mlorenzo.spring5recipeapp.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mlorenzo.spring5recipeapp.commands.IngredientCommand;
import com.mlorenzo.spring5recipeapp.converters.IngredientCommandToIngredient;
import com.mlorenzo.spring5recipeapp.converters.IngredientToIngredientCommand;
import com.mlorenzo.spring5recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.mlorenzo.spring5recipeapp.domain.Ingredient;
import com.mlorenzo.spring5recipeapp.domain.Recipe;
import com.mlorenzo.spring5recipeapp.repositories.RecipeRepository;

public class IngredientServiceImplTest {
	
	@Mock
    RecipeRepository recipeRepository;
	
	@Mock
    IngredientToIngredientCommand ingrToIngrCommandConverter;
	
	@Mock
    IngredientCommandToIngredient ingrCommandToIngrConverter;
	
	@Mock
    UnitOfMeasureCommandToUnitOfMeasure uomCommandToUomConverter;
	
	IngredientService ingredientService;
	
    @Before
    public void setUp() throws Exception {
    	// Para poder usar Mockito en esta clase de pruebas
        MockitoAnnotations.initMocks(this); // Otra opción a esta línea es anotar la clase con @ExtendWith(MockitoExtension.class)
        ingredientService = new IngredientServiceImpl(recipeRepository, ingrToIngrCommandConverter, ingrCommandToIngrConverter, uomCommandToUomConverter);
    }
    
    @Test
    public void findByRecipeIdAndReceipeIdHappyPathTest() throws Exception {
    	//given
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setRecipe(recipe);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setRecipe(recipe);
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);
        ingredient3.setRecipe(recipe);
        recipe.getIngredients().add(ingredient1);
        recipe.getIngredients().add(ingredient2);
        recipe.getIngredients().add(ingredient3);
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(ingredient3.getId());
        ingredientCommand.setRecipeId(ingredient3.getRecipe().getId());
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        when(ingrToIngrCommandConverter.convert(any(Ingredient.class))).thenReturn(ingredientCommand);
        //when
        IngredientCommand ingredientCommandReturned = ingredientService.findByRecipeIdAndIngredientId(1L, 3L);
        //then
        assertEquals(Long.valueOf(3L), ingredientCommandReturned.getId());
        assertEquals(Long.valueOf(1L), ingredientCommandReturned.getRecipeId());
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(ingrToIngrCommandConverter).convert(any(Ingredient.class));
    }
    
    @Test
    public void saveIngredientCommandTest() throws Exception {
        //given
    	IngredientCommand command = new IngredientCommand();
        command.setId(3L);
        command.setRecipeId(2L);
        Recipe savedRecipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(command.getId());
        savedRecipe.getIngredients().add(ingredient);
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(new Recipe()));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);
        when(ingrCommandToIngrConverter.convert(any(IngredientCommand.class))).thenReturn(ingredient);
        when(ingrToIngrCommandConverter.convert(any(Ingredient.class))).thenReturn(command);
        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);
        //then
        assertEquals(Long.valueOf(3L), savedCommand.getId());
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository).save(any(Recipe.class));
    }
    
    @Test
    public void deleteByIdTest() throws Exception {
    	//given
        Long idToDelete = Long.valueOf(2L);
        Long idRecipe = 3L;
        Recipe recipe = new Recipe();
        recipe.setId(idRecipe);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(idToDelete);
        ingredient.setRecipe(recipe);
        recipe.getIngredients().add(ingredient);
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        //when
        ingredientService.deleteById(idRecipe, idToDelete);
        //then
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository).save(any(Recipe.class));
    }
}
