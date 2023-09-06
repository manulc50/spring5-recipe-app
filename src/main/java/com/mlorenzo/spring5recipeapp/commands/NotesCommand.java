package com.mlorenzo.spring5recipeapp.commands;

import lombok.Getter;
import lombok.Setter;

// Clase modelo para exponer en la web

@Getter
@Setter
public class NotesCommand {
    private Long id;
    private String recipeNotes;
}
