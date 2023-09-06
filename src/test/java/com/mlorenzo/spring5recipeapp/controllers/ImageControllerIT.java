package com.mlorenzo.spring5recipeapp.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.mlorenzo.spring5recipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5recipeapp.services.ImageService;
import com.mlorenzo.spring5recipeapp.services.RecipeService;

@RunWith(SpringRunner.class)
// Nota: Por defecto, esta anotación hace que también se procesen las plantillas(En nuestro caso, plantillas Thymeleaf)
@WebMvcTest(controllers = ImageController.class)
public class ImageControllerIT {
	
	@MockBean // Crea una Mock del servicio "ImageService"  y lo inyecta en el contexto de Spring
	ImageService imageService;
	
	@MockBean // Crea una Mock del servicio "RecipeService" y lo inyecta en el contexto de Spring
	RecipeService recipeService;
	
	@Autowired
	MockMvc mockMvc;

    @Test
    public void getImageFormTest() throws Exception {
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);
        when(recipeService.findRecipeCommandById(anyLong())).thenReturn(command);
        //when
        mockMvc.perform(get("/recipe/1/image"))
        	//then
            .andExpect(status().isOk())
            .andExpect(view().name("recipe/imageUploadForm"))
            .andExpect(model().attributeExists("recipe"));
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(recipeService, times(1)).findRecipeCommandById(anyLong());

    }
    
    @Test
    public void getImageNumberFormatExceptionTest() throws Exception {
    	//when
        mockMvc.perform(get("/recipe/asdf/recipeimage"))
        	//then
            .andExpect(status().isBadRequest())
            .andExpect(view().name("400error"));
        verifyZeroInteractions(recipeService);
    }

    @Test
    public void handleImagePostTest() throws Exception {
    	//given
        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                        "Spring Framework Guru".getBytes());
        //when
        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
        	//then
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/recipe/1/show"))
            .andExpect(header().string("Location", "/recipe/1/show")); // Cuando se hace una redirección, se establece la url destino en la propiedad "Location" de la cabecera de la petición http 
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(imageService, times(1)).saveImageFile(anyLong(), any());
    }
    
    @Test
    public void renderImageFromDBTest() throws Exception {
        //given
        String s = "fake image text";
        when(imageService.getImage(anyLong())).thenReturn(s.getBytes());
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
        	//then
            .andExpect(status().isOk())
            .andReturn().getResponse();
        byte[] reponseBytes = response.getContentAsByteArray();
        assertEquals(s.getBytes().length, reponseBytes.length);
    }
}
