package com.mlorenzo.spring5recipeapp.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.mlorenzo.spring5recipeapp.domain.Recipe;
import com.mlorenzo.spring5recipeapp.repositories.RecipeRepository;

public class ImageServiceImplTest {
	
	@Mock // Crea una Mock del servicio "RecipeService"
	RecipeRepository recipeRepository;

	@InjectMocks // Esta anotación crea una instancia del servicio "ImageService" e inyecta el Mock del repositorio "recipeRepository"
    ImageServiceImpl imageService;
	
	@Before
	public void setUp() throws Exception {
		// Para poder usar Mockito en esta clase de pruebas
        MockitoAnnotations.initMocks(this); // Otra opción a esta línea es anotar la clase con @ExtendWith(MockitoExtension.class)
    }
	
	@Test
	public void saveImageFile() throws Exception {
        //given
        Long id = 1L;
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain",
        		"fake image text".getBytes());
        Recipe recipe = new Recipe();
        recipe.setId(id);
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        // Creamos una instancia del capturador de argumentos de Mockito de tipo Recipe para capturar la receta que va a ser guardada en la base de datos junto con su nueva imagen dentro del método "saveImageFile" del servicio "imageService"
        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        //when
        imageService.saveImageFile(id, multipartFile);
        //then
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        // Usamos el capturador de argumentos de Mockito para capturar el argumento de entrada del método "save" del repositorio "recipeRepository" que se corresponde con la receta que va a ser guardada en la base de datos junto con su nueva imagen dentro del método "saveImageFile" del servicio "imageService"
        verify(recipeRepository, times(1)).save(argumentCaptor.capture());   
        // Obtenemos la receta del capturador de argumentos de Mockito para realizar comprobaciones sobre ella
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }

}
