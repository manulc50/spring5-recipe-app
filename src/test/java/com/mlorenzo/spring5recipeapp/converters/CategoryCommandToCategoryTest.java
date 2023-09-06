package com.mlorenzo.spring5recipeapp.converters;


import com.mlorenzo.spring5recipeapp.commands.CategoryCommand;
import com.mlorenzo.spring5recipeapp.domain.Category;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CategoryCommandToCategoryTest {
    public static final Long ID_VALUE = 1L;
    
    CategoryCommandToCategory conveter;

    @Before
    public void setUp() throws Exception {
        conveter = new CategoryCommandToCategory();
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(conveter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(conveter.convert(new CategoryCommand()));
    }

    @Test
    public void convert() throws Exception {
        //given
        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(ID_VALUE);
        //when
        Category category = conveter.convert(categoryCommand);
        //then
        assertEquals(ID_VALUE, category.getId());
    }
}