package com.mlorenzo.spring5recipeapp.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.mlorenzo.spring5recipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5recipeapp.services.RecipeService;

public class IndexControllerTest {
	
	@Mock // Crea una Mock del servicio "RecipeService"
	RecipeService recipeService;
	
	@Mock // Crea una Mock de la interfaz "Model"
	Model model;
	
	@InjectMocks // Esta anotación crea una instancia del controlador "IndexController" e inyecta el Mock del servicio "recipeService" y el Mock del modelo "model"
	IndexController indexController;
	
	@Before
	public void setUp() {
		// Para poder usar Mockito en esta clase de pruebas
		MockitoAnnotations.initMocks(this); // Otra opción a esta línea es anotar la clase con @ExtendWith(MockitoExtension.class)
	}
	
	@Test
	public void getIndexPageMockMvcTest() throws Exception {
		//given
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
		//when
		mockMvc.perform(get("/"))
				//then
			   .andExpect(status().isOk())
			   .andExpect(view().name("index"))
			   .andExpect(model().attributeExists("recipes"));
		// Si no se indica el número de llamadas en el método "times", por defecto es 1
		verify(recipeService,times(1)).getRecipes();
	}
	
	@Test
	public void getIndexPageTest() {
		RecipeCommand recipeCommand1 = new RecipeCommand();
		recipeCommand1.setId(1L);
		RecipeCommand recipeCommand2 = new RecipeCommand();
		recipeCommand2.setId(2L);
		Set<RecipeCommand> returnedRecipeCommandSet = new HashSet<RecipeCommand>();
		returnedRecipeCommandSet.add(recipeCommand1);
		returnedRecipeCommandSet.add(recipeCommand2);
		when(recipeService.getRecipes()).thenReturn(returnedRecipeCommandSet);
		// Creamos una instancia del capturador de argumentos de Mockito de tipo Set<RecipeCommand> para capturar el conjunto o set de recetas devuelto por el servicio "recipeService" dentro del método handler "getIndexPage" del controlador "IndexController"
		ArgumentCaptor<Set<RecipeCommand>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
		String viewName = indexController.getIndexPage(model);
		assertTrue(viewName.equals("index"));
		assertEquals("index", viewName);
		// Si no se indica el número de llamadas en el método "times", por defecto es 1
		// Usamos el capturador de argumentos de Mockito para capturar el segundo argumento de entrada del método "addAttribute" del modelo que se corresponde con el conjunto o set de recetas devuelto por el servicio "recipeService" dentro del método handler "getIndexPage" del controlador "IndexController"
		verify(model).addAttribute(eq("recipes"),argumentCaptor.capture());
		verify(recipeService,times(1)).getRecipes();
		// Obtenemos el conjunto o set de recetas del capturador de argumentos de Mockito para realizar comprobaciones sobre dicho conjunto
		Set<RecipeCommand> setInController = argumentCaptor.getValue();
		assertEquals(2, setInController.size());
	}

}
