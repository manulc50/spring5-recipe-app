package com.mlorenzo.spring5recipeapp.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;

@Getter
@Setter
@Entity
@Table(name = "recipes")
public class Recipe {
	
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
	private Integer prepTime;
	private Integer cookTime;
	private Integer servings;
	private String source;
	private String url;
	
	// Esta anotación es para objetos grandes. Si se trata de una propiedad de tipo String, la columna de la tabla correspondiente de la base de datos asociada a esta propiedad se creará de tipo CLOB para permitir almacenar cadenas de caracteres de más de 255 caracteres. Sin embargo, si esta anotación se usa sobre una propiedad de tipo Byte[], el tipo de dato para la columna de la tabla de la base de datos sera BLOB, que permite almacenar una gran cantidad de bytes, como por ejemplo, archivos multimedia
	// En este caso queremos permitir que el tamaño para las direcciones de la receta sea mayor a 255 caracteres. Por eso, es necesario establecer esta anotación en esta propiedad para que la columna de la tabla correspondiente en la base de datos asociada a esta propiedad se genere con el tipo CLOB, que es un tipo de dato que permite almacenar grandes cadenas de carateres(más de 255)
	@Lob
	private String directions;
	
	// Con esta anotación indicamos a JPA cómo debe persistir en la base de datos los valores de una enumeración
	// Hay 2 opciones; ORDINAL o STRING
	// Con ORDINAL, que es la opción por defecto si no se indica la otra opción, persite los valores de la enumeración como datos numéricos, es decir, se persite un 0 para el valor EASY, un 1 para el valor MODERATE y un 2 para el valor HARD 
	// Con STRING, los valores de la enumeración se persisten como cadenas de caracteres o Strings, es decir, si el valor es EASY, se persiste "EASY", si es MODERATE, se persiste "MODERATE", y si es HARD, se persiste "HARD"
	// Es mejor persistir los valores de una enumeroación como cadenas de caracteres o String en lugar de como números porque si en un futuro se añaden nuevos valores a la enumeración, en la base de datos quedarán mal referenciados porque un valor que tenía asignado el número 3, ahora puede que sea el número 4 por el nuevo añadido de valores. Sin embargo, ésto no pasa si se almacenan en la base de datos como cadenas de caracteres o Strings porque si en un futuo se añaden más valores a la enumeración, los nombres de los valores que había antes van a seguir siendo esos 
	@Enumerated(value = EnumType.STRING)
	private Difficulty difficulty;
	// Esta anotación es para objetos grandes. Si se trata de una propiedad de tipo String, la columna de la tabla correspondiente de la base de datos asociada a esta propiedad se creará de tipo CLOB para permitir almacenar cadenas de caracteres de más de 255 caracteres. Sin embargo, si esta anotación se usa sobre una propiedad de tipo Byte[], el tipo de dato para la columna de la tabla de la base de datos sera BLOB, que permite almacenar una gran cantidad de bytes, como por ejemplo, archivos multimedia
	// En este caso queremos almacenar un archivo multimedia de tipo imagen, que son archivos con una gran cantidad de bytes. Por eso, es necesario establecer esta anotación en esta propiedad para que la columna de la tabla correspondiente en la base de datos asociada a esta propiedad se genere con el tipo BLOB, que es un tipo de dato que permite almacenar grandes cadenas de bytes
	@Lob
	private byte[] image;
	//private Byte[] image;
	
	// Relación uno a uno bidireccional con la clase entidad "Notes"
	// Hacemos que todas las operaciones que se hagan sobre una entidad de esta clase se propaguen a la entidad relacionada de la clase "Notes"
	// Esta clase entidad es la propietaria de la relación ya que en la clase entidad "Notes" se encuentra el atributo "mappedBy" haciendo referencia a esta propiedad y, por lo tanto, las claves foráneas, o extranjeras, de las entidades de la clase "Notes" estarán en la tabla de la base de datos asociada a esta clase entidad 
	@OneToOne(cascade = CascadeType.ALL)
	private Notes notes;
	
	// Relación bidireccional con la clase entidad "Ingredient"
	// Este lado de la relación se corresponde con la relación uno a muchos
	// Hacemos que todas las operaciones que se hagan sobre una entidad de esta clase se propaguen a las entidades relacionadas de la clase "Ingredient"
	// El atributo "mappedBy" se utiliza en una relación bidireccional y hace referencia a la propiedad de la clase entidad que se corresponde con el otro lado de la relación y que es dueña de esta relación
	// Es decir, en este caso "mappedBy" apunta a la propiedad "categories" de la clase entidad "Recipe" que es duña de la relación muchos a muchos con esta clase entidad
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
	private Set<Ingredient> ingredients = new HashSet<Ingredient>();
	
	// Relación muchos a muchos bidireccional con la clase entidad "Category"
	// Esta clase entidad es la propietaria de la relación ya que en la clase entidad "Category" se encuentra el atributo "mappedBy" haciendo referencia a esta propiedad
	// Con la anotación @JoinTable configuramos y persoanlizamos la tabla adicional que se crea de la relación muchos a muchos donde se relacionan los ids de las recetas con los ids de las categorías
	@ManyToMany
	@JoinTable(name = "recipe_categories", joinColumns = @JoinColumn(name = "recipe_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<Category>();
	
	public Recipe addIngredient(Ingredient ingredient){
        ingredient.setRecipe(this);
        this.ingredients.add(ingredient);
        return this;
    }
}
