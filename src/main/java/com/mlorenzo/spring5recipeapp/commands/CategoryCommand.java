package com.mlorenzo.spring5recipeapp.commands;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

// Clase modelo para exponer en la web

// Nota: Implementamos en esta clase los métodos "equals" y "hashCode" mediante Lombok para comparar las categorías de una receta
// y así poder marcar o desmarcar los checkboxes de las categorías en la plantilla "recipeForm"
@Setter
@Getter
@EqualsAndHashCode
public class CategoryCommand {
    private Long id;
    
    // Excluimos esta propiedad de la implementación de los métodos "equals" y "hashCode" que realiza Lombok
    @EqualsAndHashCode.Exclude
    private String description;
    
	/*@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof CategoryCommand))
			return false;
		CategoryCommand categoryCommand = (CategoryCommand)obj;
		return categoryCommand.getId().longValue() == this.getId().longValue();
	}*/

}
