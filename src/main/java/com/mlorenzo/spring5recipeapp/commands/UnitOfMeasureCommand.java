package com.mlorenzo.spring5recipeapp.commands;

import lombok.Getter;
import lombok.Setter;

// Clase modelo para exponer en la web

@Getter
@Setter
public class UnitOfMeasureCommand {
    private Long id;
    private String description;
}
