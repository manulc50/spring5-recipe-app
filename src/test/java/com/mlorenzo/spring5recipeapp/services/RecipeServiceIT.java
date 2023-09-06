package com.mlorenzo.spring5recipeapp.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mlorenzo.spring5recipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5recipeapp.converters.RecipeToRecipeCommand;
import com.mlorenzo.spring5recipeapp.domain.Recipe;
import com.mlorenzo.spring5recipeapp.repositories.RecipeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipeServiceIT {
	public static final String NEW_DESCRIPTION = "New Description";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;

    // Usamos la anotación @Transactional para que todas las acciones que se hagan sobre la base de datos en este método de prueba se hagan bajo una misma transacción
    // De esta manera, evitamos la excepción "org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: guru.springframework.spring5recipeapp.domain.Recipe.categories, could not initialize proxy - no Session" que se produce cuando se intenta acceder a una relación de una entidad con fetch o tipo de carga LAZY fuera de la transacción
    // Por lo tanto, con esta anotaciín nos aseguramos de que todo ocurre bajo una misma transacción
    @Transactional
    @Test
    public void saveOfDescriptionTest() throws Exception {
        //given
    	// Como no sabemos los ids de las recetas en la base de datos, recuperamos todas las recetas de la base de datos y luego cogemos la primera receta devuelta
        Iterable<Recipe> recipes = recipeRepository.findAll();
        Recipe testRecipe = recipes.iterator().next();
        RecipeCommand testRecipeCommand = recipeToRecipeCommand.convert(testRecipe);
        testRecipeCommand.setDescription(NEW_DESCRIPTION);
        //when
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(testRecipeCommand);
        //then
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
        assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());
        assertEquals(testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
    }

}
