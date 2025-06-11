package proyectosCibertec.com.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String usuario;
	private String nombre;
	private String apellido;
	private String correo;
	private String telefono;
	private String direccion;
	private String perfil;
	private String clave;
	private Date fecha; 
	private int estado = 1;
}
