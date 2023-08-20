package com.mlorenzo.spring5recipeapp.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mlorenzo.spring5recipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5recipeapp.services.ImageService;
import com.mlorenzo.spring5recipeapp.services.RecipeService;

@Controller
public class ImageController {
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
    private RecipeService recipeService;
	
	@GetMapping("recipe/{id}/image")
    public String showUploadForm(@PathVariable(name = "id") Long recipeId, Model model){
        model.addAttribute("recipe", recipeService.findRecipeCommandById(recipeId));
        return "recipe/imageUploadForm";
    }

    @PostMapping("recipe/{recipeId}/image")
    public String handleImagePost(@PathVariable Long recipeId, @RequestParam("imagefile") MultipartFile file){
        imageService.saveImageFile(recipeId, file);
        return "redirect:/recipe/" + recipeId + "/show";
    }
    
    @GetMapping("recipe/{id}/recipeimage")
    public void renderImageFromDB(@PathVariable Long id, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = recipeService.findRecipeCommandById(id);
        if (recipeCommand.getImage() != null) {
            byte[] byteArray = new byte[recipeCommand.getImage().length];
            int i = 0;
            for (Byte wrappedByte: recipeCommand.getImage()){
                byteArray[i++] = wrappedByte; //auto unboxing
            }
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
