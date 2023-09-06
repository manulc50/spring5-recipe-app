package com.mlorenzo.spring5recipeapp.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.mlorenzo.spring5recipeapp.domain.Category;
import com.mlorenzo.spring5recipeapp.domain.UnitOfMeasure;
import com.mlorenzo.spring5recipeapp.repositories.CategoryRepository;
import com.mlorenzo.spring5recipeapp.repositories.UnitOfMeasureRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Si usamos Spring Boot como en este caso, una alternativa a implementar el método "public void onApplicationEvent(ContextRefreshedEvent event)" de la interfaz de Spring "ApplicationListener<ContextRefreshedEvent>", es implementar el método "public void run(String... args)" de la interfaz de Spring Boot "CommandLineRunner"

// La interfaz "ApplicationListener<ContextRefreshedEvent>" es propia de Spring
// La interfaz CommandLineRunner es propia de Spring Boot

@Slf4j
@RequiredArgsConstructor
@Component
@Profile({"dev", "prod"}) // Esta clase sólo se tendrá en cuenta cuando arranque esta aplicación Spring Boot con el perfil "dev" o con el perfil "prod"
public class BootstapMySQL implements ApplicationListener<ContextRefreshedEvent>{
	private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (categoryRepository.count() == 0L){
            log.debug("Loading Categories");
            loadCategories();
        }
        if (unitOfMeasureRepository.count() == 0L){
            log.debug("Loading UOMs");
            loadUom();
        }
    }

    private void loadCategories(){
        Category cat1 = new Category();
        cat1.setDescription("American");
        categoryRepository.save(cat1);
        Category cat2 = new Category();
        cat2.setDescription("Italian");
        categoryRepository.save(cat2);
        Category cat3 = new Category();
        cat3.setDescription("Mexican");
        categoryRepository.save(cat3);
        Category cat4 = new Category();
        cat4.setDescription("Fast Food");
        categoryRepository.save(cat4);
    }

    private void loadUom(){
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setDescription("Teaspoon");
        unitOfMeasureRepository.save(uom1);
        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setDescription("Tablespoon");
        unitOfMeasureRepository.save(uom2);
        UnitOfMeasure uom3 = new UnitOfMeasure();
        uom3.setDescription("Cup");
        unitOfMeasureRepository.save(uom3);
        UnitOfMeasure uom4 = new UnitOfMeasure();
        uom4.setDescription("Pinch");
        unitOfMeasureRepository.save(uom4);
        UnitOfMeasure uom5 = new UnitOfMeasure();
        uom5.setDescription("Ounce");
        unitOfMeasureRepository.save(uom5);
        UnitOfMeasure uom6 = new UnitOfMeasure();
        uom6.setDescription("Each");
        unitOfMeasureRepository.save(uom6);
        UnitOfMeasure uom7 = new UnitOfMeasure();
        uom7.setDescription("Pint");
        unitOfMeasureRepository.save(uom7);
        UnitOfMeasure uom8 = new UnitOfMeasure();
        uom8.setDescription("Dash");
        unitOfMeasureRepository.save(uom8);
    }
}
