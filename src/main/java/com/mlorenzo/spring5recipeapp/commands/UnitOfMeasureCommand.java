package com.mlorenzo.spring5recipeapp.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Clase modelo para exponer en la web

@Getter
@Setter
@NoArgsConstructor
public class UnitOfMeasureCommand {
    private Long id;
    private String description;
}
