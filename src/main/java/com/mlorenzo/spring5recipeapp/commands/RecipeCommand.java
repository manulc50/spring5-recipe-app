package com.mlorenzo.spring5recipeapp.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import com.mlorenzo.spring5recipeapp.domain.Difficulty;

// Clase modelo para exponer en la web

@Getter
@Setter
public class RecipeCommand {
    private Long id;
    
    // Nota: Los mensajes de validación del archivo de propiedades "messages.properties" tienen preferencia sobre aquellos que se
    // pongan en estas anotaciones de validación mediante el atributo "message"
    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 255)
    private String description;
    
    @Min(1)
    @Max(999)
    private Integer prepTime;
    
    @Min(1)
    @Max(999)
    private Integer cookTime;
    
    @Min(1)
    @Max(100)
    private Integer servings;
    
    private String source;
    
    @URL
    private String url;
    
    @NotBlank
    private String directions;
    
    private byte[] image;
    private Set<IngredientCommand> ingredients = new HashSet<>();
    private Difficulty difficulty;
    private NotesCommand notes;
    private Set<CategoryCommand> categories = new HashSet<>();
}
