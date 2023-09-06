package com.mlorenzo.spring5recipeapp.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mlorenzo.spring5recipeapp.domain.Recipe;
import com.mlorenzo.spring5recipeapp.exceptions.NotFoundException;
import com.mlorenzo.spring5recipeapp.repositories.RecipeRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class ImageServiceImpl implements ImageService{
	private final RecipeRepository recipeRepository;

	@Override
	public void saveImageFile(Long recipeId, MultipartFile file) {
		log.debug("Received a file");
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + recipeId));
		try {
            recipe.setImage(file.getBytes());
            recipeRepository.save(recipe);
        }
		catch (IOException e) {
			//todo handle better
            log.error("Error occurred", e);
            e.printStackTrace();
        }
	}

	@Override
	public byte[] getImage(Long recipeId) {
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + recipeId));
        return recipe.getImage();
	}
}
