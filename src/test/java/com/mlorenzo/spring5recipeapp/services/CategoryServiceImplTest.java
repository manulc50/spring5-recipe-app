package com.mlorenzo.spring5recipeapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mlorenzo.spring5recipeapp.commands.CategoryCommand;
import com.mlorenzo.spring5recipeapp.converters.CategoryToCategoryCommand;
import com.mlorenzo.spring5recipeapp.domain.Category;
import com.mlorenzo.spring5recipeapp.exceptions.NotFoundException;
import com.mlorenzo.spring5recipeapp.repositories.CategoryRepository;

//Anotación para poder usar Mockito en esta clase de pruebas
@ExtendWith(MockitoExtension.class) // Otra opción a esta anotación es usar la expresión o línea "MockitoAnnotations.initMocks(this);" en el método "setUp"
public class CategoryServiceImplTest {
	
	@Mock
    CategoryRepository categoryRepository;
	
	CategoryToCategoryCommand categoryToCategoryCommand = new CategoryToCategoryCommand();
    
	CategoryService service;
	
	@BeforeEach
    void setUp() throws Exception {
		service = new CategoryServiceImpl(categoryRepository,categoryToCategoryCommand);
	}
	
	@Test
	void getCategoriesTest() {
		//given
		Category cat1 = new Category();
		cat1.setId(1L);
		Category cat2 = new Category();
		cat2.setId(2L);
		Set<Category> returnedSet = new HashSet<Category>();
		returnedSet.add(cat1);
		returnedSet.add(cat2);
		when(categoryRepository.findAll()).thenReturn(returnedSet);
		//when
		Set<CategoryCommand> categoryCommands = service.getCategories();
		//then
		assertEquals(2, categoryCommands.size());
		// Si no se indica el número de llamadas en el método "times", por defecto es 1
		verify(categoryRepository).findAll();
	}
	
	@Test
    void getCategoryByIdTest() throws Exception {
        Category categoryReturned = new Category();
        categoryReturned.setId(1L);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryReturned));
        CategoryCommand categoryCommand = service.getCategoryById(1L);
        assertNotNull(categoryCommand, "Null recipe returned");
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(categoryRepository, times(1)).findById(anyLong());
        verify(categoryRepository, never()).findAll();
    }
	
	@Test
    void getCategoryByIdNotFoundTest() {
    	Optional<Category> categoryOptional = Optional.empty();
    	when(categoryRepository.findById(anyLong())).thenReturn(categoryOptional);
    	assertThrows(NotFoundException.class, () -> {
        	//should go boom
    		service.getCategoryById(1L);
        });
    	// Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(categoryRepository, times(1)).findById(anyLong());
    }

}
