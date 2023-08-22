package com.mlorenzo.spring5recipeapp.repositories;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mlorenzo.spring5recipeapp.domain.UnitOfMeasure;

@RunWith(SpringRunner.class)
// Anotación que permite realizar pruebas de integración de la capa de repositorios que utilizan Spring Data JPA
// Para ello, esta anotación configura Spring Data JPA por nosotros y utiliza una base de datos embebida(H2 en nuestro caso) para realizar las pruebas
@DataJpaTest
public class UnitOfMeasureRepositoryIT {
	
	// Podemos inyectar el bean con la implementación del respositorio "UnitOfMeasureRepository" porque la anotación "@DataJpaTest" levanta y configura el contexto de la aplicación correspondiente a los repositorios con Spring Data JPA por nosotros. No levanta todo el contexto de la aplicación, sólo la parte de los repositorios con Spring Data JPA
	@Autowired
	UnitOfMeasureRepository uomRepository;
	
	@Test
	public void findByDescriptionTest() {
		Optional<UnitOfMeasure> uomOptional = uomRepository.findByDescription("Teaspoon");
		assertEquals("Teaspoon", uomOptional.get().getDescription());
	}
	
	@Test
	public void findByDescriptionCupTest() {
		Optional<UnitOfMeasure> uomOptional = uomRepository.findByDescription("Cup");
		assertEquals("Cup", uomOptional.get().getDescription());
	}
}
