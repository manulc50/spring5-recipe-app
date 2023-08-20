package com.mlorenzo.spring5recipeapp.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.mlorenzo.spring5recipeapp.commands.CategoryCommand;
import com.mlorenzo.spring5recipeapp.domain.Category;

// Spring Framework tiene la interfaz converter que pasa o transfiere los datos de objetos de un tipo a objetos de otro tipo que tienen propiedades en común
// En este caso, pasamos los datos de objetos de tipo CategoryCommand a objetos de tipo Category
// La interfaz Converter de Spring Framework es una alternativa a usar la librería MapStruct

@Component
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category>{

    @Synchronized // Spring no garantiza la seguridad del hilo. Por lo tanto, usamos esta anotación de Lombok para que el método esté sincronizado sin usar hilos
    @Nullable
    @Override
    public Category convert(CategoryCommand source) {
        if (source == null) {
            return null;
        }
        final Category category = new Category();
        category.setId(source.getId());
        category.setDescription(source.getDescription());
        return category;
    }
}
