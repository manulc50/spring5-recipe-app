package com.mlorenzo.spring5recipeapp.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mlorenzo.spring5recipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5recipeapp.services.ImageService;
import com.mlorenzo.spring5recipeapp.services.RecipeService;

public class ImageControllerTest {
	
	@Mock // Crea una Mock del servicio "ImageService"
	ImageService imageService;
	
	@Mock // Crea una Mock del servicio "RecipeService"
	RecipeService recipeService;
	
	@InjectMocks // Esta anotación crea una instancia del controlador "ImageController" e inyecta los Mocks de los servicios "imageService" y "recipeService"
	ImageController controller;
	
	MockMvc mockMvc;
	 
	@BeforeEach
	void setUp() throws Exception {
		// Para poder usar Mockito en esta clase de pruebas
		MockitoAnnotations.initMocks(this); // Otra opción a esta línea es anotar la clase con @ExtendWith(MockitoExtension.class)
	    mockMvc = MockMvcBuilders.standaloneSetup(controller)
	    		.setControllerAdvice(new ControllerExceptionHandler())
	    		.build();
	}

    @Test
    void getImageFormTest() throws Exception {
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);
        when(recipeService.findRecipeCommandById(anyLong())).thenReturn(command);
        //when
        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/imageUploadForm"))
                .andExpect(model().attributeExists("recipe"));
        //then
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeService, times(1)).findRecipeCommandById(anyLong());

    }
    
    @Test
    void getImageNumberFormatExceptionTest() throws Exception {
        mockMvc.perform(get("/recipe/asdf/recipeimage"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
        verifyNoInteractions(recipeService);
    }

    @Test
    void handleImagePostTest() throws Exception {
    	//given
        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                        "Spring Framework Guru".getBytes());
        //when
        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/show"))
                .andExpect(header().string("Location", "/recipe/1/show")); // Cuando se hace una redirección, se establece la url destino en la propiedad "Location" de la cabecera de la petición http
        //then
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(imageService, times(1)).saveImageFile(anyLong(), any());
    }
    
    @Test
    void renderImageFromDBTest() throws Exception {
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);
        String s = "fake image text";
        Byte[] bytesBoxed = new Byte[s.getBytes().length];
        int i = 0;
        for (byte primByte : s.getBytes()){
            bytesBoxed[i++] = primByte;
        }
        command.setImage(bytesBoxed);
        when(recipeService.findRecipeCommandById(anyLong())).thenReturn(command);
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        //then
        byte[] reponseBytes = response.getContentAsByteArray();
        assertEquals(s.getBytes().length, reponseBytes.length);
    }
}
