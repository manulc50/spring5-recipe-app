package com.mlorenzo.spring5recipeapp.services;

import java.util.Set;

import com.mlorenzo.spring5recipeapp.commands.CategoryCommand;

public interface CategoryService {
	Set<CategoryCommand> getCategories();
	CategoryCommand getCategoryById(Long id);
}
