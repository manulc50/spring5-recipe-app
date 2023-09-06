package com.mlorenzo.spring5recipeapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mlorenzo.spring5recipeapp.commands.NotesCommand;
import com.mlorenzo.spring5recipeapp.domain.Notes;

// Spring Framework tiene la interfaz converter que pasa o transfiere los datos de objetos de un tipo a objetos de otro tipo que tienen propiedades en común
// En este caso, pasamos los datos de objetos de tipo Notes a objetos de tipo NotesCommand
// La interfaz Converter de Spring Framework es una alternativa a usar la librería MapStruct

@Component
public class NotesToNotesCommand implements Converter<Notes, NotesCommand>{

    @Override
    public NotesCommand convert(Notes source) {
        if (source == null) {
            return null;
        }
        final NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId(source.getId());
        notesCommand.setRecipeNotes(source.getRecipeNotes());
        return notesCommand;
    }
}
