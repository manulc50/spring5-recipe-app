package com.mlorenzo.spring5recipeapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mlorenzo.spring5recipeapp.commands.CategoryCommand;

import lombok.RequiredArgsConstructor;

// Esta clase se usa para convertir los valores de los checkbox de las categorías del fomulario para crear o editar una receta, que son los ids de esas categorías, en objetos de tipo CategoryCommand
// Como se crea un bean de esta clase con la anotación @Componente y está dentro del contexto de Spring, esta conversión se hará de forma automática siempre que sea requiera 

@RequiredArgsConstructor
@Component
public class StringToCategoryCommand implements Converter<String,CategoryCommand>{

	@Override
	public CategoryCommand convert(String id) {
		CategoryCommand categoryCommand = new CategoryCommand();
		categoryCommand.setId(Long.valueOf(id));
		return categoryCommand;
	}

}
