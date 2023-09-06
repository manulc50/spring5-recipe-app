package com.mlorenzo.spring5recipeapp.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mlorenzo.spring5recipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5recipeapp.converters.RecipeCommandToRecipe;
import com.mlorenzo.spring5recipeapp.converters.RecipeToRecipeCommand;
import com.mlorenzo.spring5recipeapp.domain.Recipe;
import com.mlorenzo.spring5recipeapp.exceptions.NotFoundException;
import com.mlorenzo.spring5recipeapp.repositories.RecipeRepository;

public class RecipeServiceImplTest {

    @Mock
	RecipeRepository recipeRepository;
    
    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;
    
    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;
	
    RecipeServiceImpl recipeService;
	
	@Before
	public void setUp() {
		// Para poder usar Mockito en esta clase de pruebas
		MockitoAnnotations.initMocks(this); // Otra opción a esta línea es anotar la clase con @ExtendWith(MockitoExtension.class)
		recipeService = new RecipeServiceImpl(recipeRepository,recipeCommandToRecipe,recipeToRecipeCommand);
	}

	@Test
	public void getRecipesTest() {
		Recipe recipe1 = new Recipe();
		recipe1.setId(1L);
		Recipe recipe2 = new Recipe();
		recipe2.setId(2L);
		RecipeCommand command1 = new RecipeCommand();
		command1.setId(recipe1.getId());
		RecipeCommand command2 = new RecipeCommand();
		command2.setId(recipe2.getId());
		Set<Recipe> returnedRecipesSet = new HashSet<Recipe>();
		returnedRecipesSet.add(recipe1);
		returnedRecipesSet.add(recipe2);
		when(recipeRepository.findAll()).thenReturn(returnedRecipesSet);
		when(recipeToRecipeCommand.convert(any(Recipe.class))).thenReturn(command1).thenReturn(command2);
		Set<RecipeCommand> recipeCommands = recipeService.getRecipes();
		assertEquals(2, recipeCommands.size());
		// Si no se indica el número de llamadas en el método "times", por defecto es 1
		verify(recipeRepository, times(1)).findAll();
		verify(recipeToRecipeCommand, times(2)).convert(any(Recipe.class));
	}
	
	@Test
	public void getRecipeByIdTest() throws Exception {
        Recipe recipeReturned = new Recipe();
        recipeReturned.setId(1L);
        RecipeCommand commandReturned = new RecipeCommand();
        commandReturned.setId(recipeReturned.getId());
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipeReturned));
        when(recipeToRecipeCommand.convert(any(Recipe.class))).thenReturn(commandReturned);
        RecipeCommand recipeCommand = recipeService.findRecipeCommandById(1L);
        assertNotNull("Null recipe returned", recipeCommand);
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
        verify(recipeToRecipeCommand).convert(any(Recipe.class));
    }
	
	@Test(expected = NotFoundException.class)
	public void getRecipeByIdNotFoundTest() {
    	Optional<Recipe> recipeOptional = Optional.empty();
    	when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        //should go boom
    	recipeService.findRecipeCommandById(1L);
    	// Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeRepository, times(1)).findById(anyLong());	
    }
	

    @Test
    public void deleteByIdTest() throws Exception {
        Long idToDelete = Long.valueOf(2L);
        recipeService.deleteById(idToDelete);
        //no 'when' method, since method has void return type
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeRepository, times(1)).deleteById(anyLong());
    }
}
