package com.mlorenzo.spring5recipeapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.mlorenzo.spring5recipeapp.commands.IngredientCommand;
import com.mlorenzo.spring5recipeapp.domain.Ingredient;

// Spring Framework tiene la interfaz converter que pasa o transfiere los datos de objetos de un tipo a objetos de otro tipo que tienen propiedades en común
// En este caso, pasamos los datos de objetos de tipo IngredientCommand a objetos de tipo Ingredient
// La interfaz Converter de Spring Framework es una alternativa a usar la librería MapStruct

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {
    private final UnitOfMeasureCommandToUnitOfMeasure uomConverter;

    public IngredientCommandToIngredient(UnitOfMeasureCommandToUnitOfMeasure uomConverter) {
        this.uomConverter = uomConverter;
    }

    @Nullable
    @Override
    public Ingredient convert(IngredientCommand source) {
        if (source == null) {
            return null;
        }
        final Ingredient ingredient = new Ingredient();
        ingredient.setId(source.getId());
        ingredient.setAmount(source.getAmount());
        ingredient.setDescription(source.getDescription());
        ingredient.setUom(uomConverter.convert(source.getUom()));
        return ingredient;
    }
}
