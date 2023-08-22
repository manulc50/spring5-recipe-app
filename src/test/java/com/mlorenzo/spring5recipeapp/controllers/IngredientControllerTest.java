package com.mlorenzo.spring5recipeapp.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mlorenzo.spring5recipeapp.commands.IngredientCommand;
import com.mlorenzo.spring5recipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5recipeapp.services.IngredientService;
import com.mlorenzo.spring5recipeapp.services.RecipeService;
import com.mlorenzo.spring5recipeapp.services.UnitOfMeasureService;

public class IngredientControllerTest {
	
	@Mock // Crea una Mock del servicio "RecipeService"
    RecipeService recipeService;
	
	@Mock // Crea una Mock del servicio "ingredientService"
    IngredientService ingredientService;
	
	@Mock // Crea una Mock del servicio "unitOfMeasureService"
	UnitOfMeasureService unitOfMeasureService;

	@InjectMocks // Esta anotación crea una instancia del controlador "IngredientController" e inyecta los Mocks de los servicios "recipeService","ingredientService" y "unitOfMeasureService"
    IngredientController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
    	//Para poder usar Mockito en esta clase de pruebas
        MockitoAnnotations.initMocks(this); // Otra opción a esta línea es anotar la clase con @ExtendWith(MockitoExtension.class)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void listIngredientsTest() throws Exception {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        when(recipeService.findRecipeCommandById(anyLong())).thenReturn(recipeCommand);
        //when
        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));
        //then
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeService, times(1)).findRecipeCommandById(anyLong());
    }
    
    @Test
    public void showIngredientTest() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);
        //when
        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));
        //then
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyLong(), anyLong());
    }
    
    @Test
    public void newIngredientFormTest() throws Exception {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        when(recipeService.findRecipeCommandById(anyLong())).thenReturn(recipeCommand);
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());
        //when
        mockMvc.perform(get("/recipe/1/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientForm"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
        //then
        verify(recipeService, times(1)).findRecipeCommandById(anyLong());

    }
    
    @Test
    public void updateIngredientFormTest() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());
        //when
        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientForm"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
        //then
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyLong(), anyLong());
        verify(unitOfMeasureService, times(1)).listAllUoms();
    }
    
    @Test
    public void saveOrUpdateTest() throws Exception {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId(3L);
        command.setRecipeId(2L);
        when(ingredientService.saveIngredientCommand(any())).thenReturn(command);
        //when
        mockMvc.perform(post("/recipe/2/ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some string"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/ingredient/3/show"));
        //then
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(ingredientService).saveIngredientCommand(any());
    }
    
    @Test
    public void deleteIngredientTest() throws Exception {
    	//when
    	mockMvc.perform(get("/recipe/2/ingredient/3/delete"))
    		   .andExpect(status().is3xxRedirection())
    		   .andExpect(view().name("redirect:/recipe/2/ingredients"));
    	//then
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(ingredientService).deleteById(anyLong(), anyLong());
    }
}
