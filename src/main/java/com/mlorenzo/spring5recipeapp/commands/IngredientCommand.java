package com.mlorenzo.spring5recipeapp.commands;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

// Clase modelo para exponer en la web

@Getter
@Setter
public class IngredientCommand {
    private Long id;
    private Long recipeId;
    private String description;
    private BigDecimal amount;
    private UnitOfMeasureCommand uom;
}
