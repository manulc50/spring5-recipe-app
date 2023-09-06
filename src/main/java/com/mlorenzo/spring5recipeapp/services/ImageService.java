package com.mlorenzo.spring5recipeapp.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
	void saveImageFile(Long recipeId,MultipartFile file);
	byte[] getImage(Long recipeId);
}
