package com.mlorenzo.spring5recipeapp.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CategoryTest {
	Category category;
	
	@BeforeEach
	void setUp() {
		category = new Category();
	}
	
	@Test
	void getIdTest() {
		Long idValue = 4L;
		category.setId(idValue);
		assertEquals(idValue, category.getId());
	}

}
