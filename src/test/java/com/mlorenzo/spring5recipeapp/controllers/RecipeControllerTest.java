package com.mlorenzo.spring5recipeapp.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mlorenzo.spring5recipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5recipeapp.exceptions.NotFoundException;
import com.mlorenzo.spring5recipeapp.services.CategoryService;
import com.mlorenzo.spring5recipeapp.services.RecipeService;

public class RecipeControllerTest {
	
	@Mock // Crea una Mock del servicio "RecipeService"
    RecipeService recipeService;
	
	@Mock // Crea una Mock del servicio "CategoryService"
    CategoryService categoryService;

    RecipeController controller;
    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
    	//Para poder usar Mockito en esta clase de pruebas
        MockitoAnnotations.initMocks(this); // Otra opción a esta línea es anotar la clase con @ExtendWith(MockitoExtension.class)
        controller = new RecipeController(recipeService,categoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
        		.setControllerAdvice(new ControllerExceptionHandler())
        		.build();
    }

    @Test
    public void getRecipeTest() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        when(recipeService.findRecipeCommandById(anyLong())).thenReturn(recipeCommand);
        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeService, times(1)).findRecipeCommandById(anyLong());
    }
    
    @Test
    public void getRecipeNotFoundTest() throws Exception {
        when(recipeService.findRecipeCommandById(anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeService).findRecipeCommandById(anyLong());
    }
    
    @Test
    public void getRecipeNumberFormatExceptionTest() throws Exception {
        mockMvc.perform(get("/recipe/aaa/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
        verifyZeroInteractions(recipeService);
    }
    
    @Test
    public void getNewRecipeFormTest() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeForm"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attributeExists("categorySet"));
        verifyZeroInteractions(recipeService);
    }
    
    @Test
    public void postNewRecipeFormTest() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(2L);
        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(command);
        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some string")
                .param("directions", "some directions"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/show"));
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeService).saveRecipeCommand(any(RecipeCommand.class));
    }
    
    @Test
    public void postNewRecipeFormValidationFailTest() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(2L);
        when(recipeService.saveRecipeCommand(any())).thenReturn(command);
        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipeForm"));
    }
    
    @Test
    public void getUpdateViewTest() throws Exception {
        RecipeCommand recipeCommandreturned = new RecipeCommand();
        recipeCommandreturned.setId(2L);
        when(recipeService.findRecipeCommandById(anyLong())).thenReturn(recipeCommandreturned);
        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeForm"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attributeExists("categorySet"));
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeService).findRecipeCommandById(anyLong());
    }
    
    @Test
    public void deleteActionTest() throws Exception {
        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeService, times(1)).deleteById(anyLong());
    }

}
