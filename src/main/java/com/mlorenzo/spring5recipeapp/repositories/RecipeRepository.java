package com.mlorenzo.spring5recipeapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.mlorenzo.spring5recipeapp.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long>{

}
