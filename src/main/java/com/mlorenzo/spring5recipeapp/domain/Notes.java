package com.mlorenzo.spring5recipeapp.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//El método "hasCode" que proporciona Lombok tiene alguna incompatibilidad con las relaciones JPA bidireccionales produciéndose un error de ejecución de la aplicación
//Por esta razón, indicamos que se excluya la propiedad "recipe", que es la propiedad que establece la relación bidireccional, de los métodos "equals"(Si se quita del método "hasCode", también se tiene que quitar del método "equals". Ambos métodos van de la mano en Lombok) y "hasCode"
//Sólo es necesario excluir la propiedad en un lado de la relación bidireccional, es decir, como ya lo hemos hecho en esta clase entidad "Notes", no hace falta excluir la propiedad "notes" de la clase entidad "Recipe"
//@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Notes {
	
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
	
	// Relación uno a uno bidireccional con la clase entidad "Recipe"
	// El atributo "mappedBy" se utiliza en una relación bidireccional y hace referencia a la propiedad de la clase entidad que se corresponde con el otro lado de la relación y que es dueña de esta relación
	// Es decir, en este caso "mappedBy" apunta a la propiedad "notes" de la clase entidad "Recipe" que es dueña de la relación uno a uno con esta clase entidad
	@OneToOne(mappedBy = "notes")
	private Recipe recipe;
	
	// Esta anotación es para objetos grandes. Si se trata de una propiedad de tipo String, la columna de la tabla correspondiente de la base de datos asociada a esta propiedad se creará de tipo CLOB para permitir almacenar cadenas de caracteres de más de 255 caracteres. Sin embargo, si esta anotación se usa sobre una propiedad de tipo Byte[], el tipo de dato para la columna de la tabla de la base de datos sera BLOB, que permite almacenar una gran cantidad de bytes, como por ejemplo, archivos multimedia
	// En este caso queremos permitir que el tamaño para las notas de la receta sea mayor a 255 caracteres. Por eso, es necesario establecer esta anotación en esta propiedad para que la columna de la tabla correspondiente en la base de datos asociada a esta propiedad se genere con el tipo CLOB, que es un tipo de dato que permite almacenar grandes cadenas de carateres(más de 255)
	@Lob
	private String recipeNotes;
}
