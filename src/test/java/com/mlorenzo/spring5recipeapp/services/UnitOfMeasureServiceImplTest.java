package com.mlorenzo.spring5recipeapp.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mlorenzo.spring5recipeapp.commands.UnitOfMeasureCommand;
import com.mlorenzo.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.mlorenzo.spring5recipeapp.domain.UnitOfMeasure;
import com.mlorenzo.spring5recipeapp.repositories.UnitOfMeasureRepository;

public class UnitOfMeasureServiceImplTest {
	
	@Mock
    UnitOfMeasureRepository unitOfMeasureRepository;
	
	UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();    
	UnitOfMeasureService service;
	
	@Before
	public void setUp() throws Exception {
		// Para poder usar Mockito en esta clase de pruebas
        MockitoAnnotations.initMocks(this); // Otra opción a esta línea es anotar la clase con @ExtendWith(MockitoExtension.class)
        service = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
    }
	
	@Test
	public void listAllUomsTest() throws Exception {
        //given
        Set<UnitOfMeasure> unitOfMeasures = new HashSet<UnitOfMeasure>();
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId(1L);
        unitOfMeasures.add(uom1);
        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId(2L);
        unitOfMeasures.add(uom2);
        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);
        //when
        Set<UnitOfMeasureCommand> commands = service.listAllUoms();
        //then
        assertEquals(2, commands.size());
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(unitOfMeasureRepository, times(1)).findAll();
    }

}
