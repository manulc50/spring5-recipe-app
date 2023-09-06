package com.mlorenzo.spring5recipeapp.services;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.mlorenzo.spring5recipeapp.commands.CategoryCommand;
import com.mlorenzo.spring5recipeapp.converters.CategoryToCategoryCommand;
import com.mlorenzo.spring5recipeapp.domain.Category;
import com.mlorenzo.spring5recipeapp.exceptions.NotFoundException;
import com.mlorenzo.spring5recipeapp.repositories.CategoryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{
	private final CategoryRepository categoryRepository;
	private final CategoryToCategoryCommand categoryToCategoryCommand;

	@Override
	public Set<CategoryCommand> getCategories() {
		// Usamos la clase StreamSupport para obtener un stream a partir del iterable devuelto por el método "findAll" del repositorio "categoryRepository"
		// De esta manera, podemos usar luego el operador o método "map" sobre dicho stream
		return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
				.map(categoryToCategoryCommand::convert) // Versión simplificada de la expresión "category -> categoryToCategoryCommand.convert(category)"
				.collect(Collectors.toSet());	
	}

	@Override
	public CategoryCommand getCategoryById(Long id) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category Not Found for id value: " + id));
		return categoryToCategoryCommand.convert(category);
	}
}
