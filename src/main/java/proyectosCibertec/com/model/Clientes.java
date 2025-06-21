package proyectosCibertec.com.model;

import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="clientes")
public class Clientes {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int id;
	 private String dni;
	 private String nombre;
	 private String telefono;
	 private String direccion;
	 private Date fecha; 
	 private int estado;
}
