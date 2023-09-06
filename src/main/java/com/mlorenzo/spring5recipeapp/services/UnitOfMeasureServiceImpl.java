package com.mlorenzo.spring5recipeapp.services;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.mlorenzo.spring5recipeapp.commands.UnitOfMeasureCommand;
import com.mlorenzo.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.mlorenzo.spring5recipeapp.repositories.UnitOfMeasureRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService{
	private final UnitOfMeasureRepository unitOfMeasureRepository;
	private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

	@Override
	public Set<UnitOfMeasureCommand> listAllUoms() {
		// Usamos la clase StreamSupport para obtener un stream a partir del iterable devuelto por el método "findAll" del repositorio "unitOfMeasureRepository"
		// De esta manera, podemos usar luego el operador o método "map" sobre dicho stream
		return StreamSupport.stream(unitOfMeasureRepository.findAll().spliterator(), false)
                .map(unitOfMeasureToUnitOfMeasureCommand::convert) // Versión simplificada de la expresión "uom -> unitOfMeasureToUnitOfMeasureCommand.convert(uom)"
                .collect(Collectors.toSet());
	}
}
