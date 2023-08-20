package com.mlorenzo.spring5recipeapp.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mlorenzo.spring5recipeapp.commands.CategoryCommand;
import com.mlorenzo.spring5recipeapp.services.CategoryService;

// Esta clase se usa para convertir los valores de los checkbox de las categorías del fomulario para crear o editar una receta, que son los ids de esas categorías, en objetos de tipo CategoryCommand
// Como se crea un bean de esta clase con la anotación @Componente y está dentro del contexto de Spring, esta conversión se hará de forma automática siempre que sea requiera 

@Component
public class StringToCategoryCommand implements Converter<String,CategoryCommand>{
	
	@Autowired
	private CategoryService categoryService;

	@Override
	public CategoryCommand convert(String id) {
		return categoryService.getCategoryById(Long.valueOf(id));
	}

}
