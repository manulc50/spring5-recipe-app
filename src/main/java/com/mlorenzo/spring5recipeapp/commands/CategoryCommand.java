package com.mlorenzo.spring5recipeapp.commands;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Clase modelo para exponer en la web

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class CategoryCommand {
    private Long id;
    private String description;
}
