package com.mlorenzo.spring5recipeapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mlorenzo.spring5recipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5recipeapp.converters.CategoryCommandToCategory;
import com.mlorenzo.spring5recipeapp.converters.CategoryToCategoryCommand;
import com.mlorenzo.spring5recipeapp.converters.IngredientCommandToIngredient;
import com.mlorenzo.spring5recipeapp.converters.IngredientToIngredientCommand;
import com.mlorenzo.spring5recipeapp.converters.NotesCommandToNotes;
import com.mlorenzo.spring5recipeapp.converters.NotesToNotesCommand;
import com.mlorenzo.spring5recipeapp.converters.RecipeCommandToRecipe;
import com.mlorenzo.spring5recipeapp.converters.RecipeToRecipeCommand;
import com.mlorenzo.spring5recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.mlorenzo.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.mlorenzo.spring5recipeapp.domain.Recipe;
import com.mlorenzo.spring5recipeapp.exceptions.NotFoundException;
import com.mlorenzo.spring5recipeapp.repositories.RecipeRepository;

public class RecipeServiceImplTest {

    @Mock
	RecipeRepository recipeRepository;
    
    RecipeCommandToRecipe recipeCommandToRecipe;
    RecipeToRecipeCommand recipeToRecipeCommand;
	RecipeServiceImpl recipeService;
	
	@BeforeEach
	void setUp() {
		// Para poder usar Mockito en esta clase de pruebas
		MockitoAnnotations.initMocks(this); // Otra opción a esta línea es anotar la clase con @ExtendWith(MockitoExtension.class)
		recipeService = new RecipeServiceImpl(recipeRepository,recipeCommandToRecipe,recipeToRecipeCommand);
	}
	
	//init converters
	public RecipeServiceImplTest() {
		recipeCommandToRecipe = new RecipeCommandToRecipe(new CategoryCommandToCategory(),
				new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()),
				new NotesCommandToNotes());
		recipeToRecipeCommand = new RecipeToRecipeCommand(new CategoryToCategoryCommand(),
	    		new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand()),
	    		new NotesToNotesCommand());
	}

	@Test
	void getRecipesTest() {
		Recipe recipe1 = new Recipe();
		recipe1.setId(1L);
		Recipe recipe2 = new Recipe();
		recipe2.setId(2L);
		Set<Recipe> returnedRecipesSet = new HashSet<Recipe>();
		returnedRecipesSet.add(recipe1);
		returnedRecipesSet.add(recipe2);
		when(recipeRepository.findAll()).thenReturn(returnedRecipesSet);
		Set<RecipeCommand> recipeCommands = recipeService.getRecipes();
		assertEquals(2, recipeCommands.size());
		// Si no se indica el número de llamadas en el método "times", por defecto es 1
		verify(recipeRepository, times(1)).findAll();
	}
	
	@Test
    void getRecipeByIdTest() throws Exception {
        Recipe recipeReturned = new Recipe();
        recipeReturned.setId(1L);
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipeReturned));
        RecipeCommand recipeCommand = recipeService.findRecipeCommandById(1L);
        assertNotNull(recipeCommand, "Null recipe returned");
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }
	
	@Test
    void getRecipeByIdNotFoundTest() {
    	Optional<Recipe> recipeOptional = Optional.empty();
    	when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
    	assertThrows(NotFoundException.class, () -> {
        	//should go boom
    		recipeService.findRecipeCommandById(1L);
        });
    	// Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeRepository, times(1)).findById(anyLong());	
    }
	

    @Test
    void deleteByIdTest() throws Exception {
        Long idToDelete = Long.valueOf(2L);
        recipeService.deleteById(idToDelete);
        //no 'when' method, since method has void return type
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeRepository, times(1)).deleteById(anyLong());
    }
}
