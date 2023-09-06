package com.mlorenzo.spring5recipeapp.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//El método "hasCode" que proporciona Lombok tiene alguna incompatibilidad con las relaciones JPA bidireccionales produciéndose un error de ejecución de la aplicación
//Por esta razón, indicamos que se excluya la propiedad "recipe", que es la propiedad que establece la relación bidireccional, de los métodos "equals"(Si se quita del método "hasCode", también se tiene que quitar del método "equals". Ambos métodos van de la mano en Lombok) y "hasCode"
//Sólo es necesario excluir la propiedad en un lado de la relación bidireccional, es decir, como ya lo hemos hecho en esta clase entidad "Ingredient", no hace falta excluir la propiedad "ingredients" de la clase entidad "Recipe"
//@EqualsAndHashCode(exclude = {"recipe"})
@Entity
@Table(name = "ingredients")
public class Ingredient {
	
	// En las relaciones ManyToOne y OneToOne, el tipo de fetch o de carga por defecto es EAGER(carga ansiosa)
    // En las relaciones OneToMany y ManyToMany , el tipo de fetch o de carga por defecto es LAZY(carga perezosa)
	
	// Indica a JPA que este propiedad representa la clave primaria
	// La estrategia de generación de los valores de este identificador puede ser de tipo "TABLE","SEQUENCE","IDENTITY" o "AUTO"
	// La estrategia "TABLE": Simula una secuencia almacenando y actualizando su valor actual en una tabla de base de datos que requiere el uso de bloqueos pesimistas que colocan todas las transacciones en un orden secuencial . Esto ralentiza su aplicación.
	// La estrategia "SEQUENCE": Utiliza una secuencia de bases de datos para generar valores únicos. Requiere sentencias select adicionales para obtener el siguiente valor de una secuencia de base de datos. Pero esto no tiene impacto en el rendimiento para la mayoría de las aplicaciones. Y si su aplicación tiene que persistir una gran cantidad de nuevas entidades, puede usar algunas optimizaciones especificas de hibernate para reducir la cantidad de declaraciones.
	// La estrategia "IDENTITY": Se basa en una columna de base de datos con incremento automático y permite que la base de datos genere un nuevo valor con cada operación de inserción. Desde el punto de vista de la base de datos, esto es muy eficiente porque las columnas de incremento automático están altamente optimizadas y no requiere ninguna declaración adicional. Este enfoque tiene un inconveniente importante si usa Hibernate, ya que requiere un valor de clave principal para cada entidad administrada y, por lo tanto, debe realizar la instrucción de inserción de inmediato. Esto evita que utilice diferentes técnicas de optimización  como el procesamiento por lotes JDBC.
	// La estrategia "AUTO": El GenerationType.AUTO es el tipo de generación por defecto y permite que el proveedor de persistencia elegir la estrategia de generación. Si usa Hibernate como su proveedor de persistencia, selecciona una estrategia de generación basada en el dialecto específico de la base de datos.
	// En una base de datos MySQL no se puede usar la estrategia "SEQUENCE" porque requiere una secuencia de base de datos y MySQL no admite esta función. Por lo tanto, debe elegir entre IDENTITY y TABLE . Esa es una decisión fácil considerando los problemas de rendimiento y escalabilidad de la estrategia TABLE.
	// Si está trabajando con una base de datos MySQL, siempre debe usar GenerationType.IDENTITY . Utiliza una columna de base de datos auto_increment y es el enfoque más eficiente disponible. Puede hacerlo anotando su atributo de clave principal con @GeneratedValue (estrategia = GenerationType.IDENTITY).
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String description;
	private BigDecimal amount;
	
	// Relación uno a uno unidireccional con la clase entidad "UnitOfMeasure"
	// Establecemos la carga desde la base de datos de la entidad de la clase "UnitOfMeasure" relacionada en EAGER(carga ansiosa).De esta manera, se cargará de la base de datos esta entidad al mismo tiempo que se solicite la carga de la base de datos de la entidad de esta clase
	// Nota: Se indica el tipo de carga a modo de ejemplo pero se puede omitir porque por defecto, para relaciones de tipo uno a uno, el tipo de carga es EAGER, tal y como indicamos en el atributo "fetch" de la anotación
	@OneToOne(fetch = FetchType.EAGER)
	private UnitOfMeasure uom;
	
	// Relación bidireccional con la clase entidad "Recipe"
	// Este lado de la relación se corresponde con la relación mucho a uno
	// Esta clase entidad es la propietaria de la relación ya que en la clase entidad "Recipe" se encuentra el atributo "mappedBy" haciendo referencia a esta propiedad y, por lo tanto, las claves foráneas, o extranjeras, de las entidades de la clase "Recipe" estarán en la tabla de la base de datos asociada a esta clase entidad 
	@ManyToOne
	private Recipe recipe;
	
	public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom) {
		this.description = description;
		this.amount = amount;
		this.uom = uom;
	}
}
