package com.mlorenzo.spring5recipeapp.services;

import java.util.Set;

import com.mlorenzo.spring5recipeapp.commands.UnitOfMeasureCommand;

public interface UnitOfMeasureService {
	Set<UnitOfMeasureCommand> listAllUoms();
}
