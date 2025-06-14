package proyectosCibertec.com.model;


import java.time.LocalDateTime;


import jakarta.persistence.Column;
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
	private Integer id;
	@Column(name = "usuario", length = 20, nullable = false)
	private String nomUsuario;
	@Column(length = 100, nullable = false)
	private String nombre;
	private String apellido;
	@Column(length = 80, nullable = false)
	private String correo;
	private String telefono;
	private String direccion;
	private String perfil = "avatar.svg";
	@Column(length = 100, nullable = false)
	private String clave;
	@Column(nullable = false)
	private LocalDateTime fecha = LocalDateTime.now();
	private int estado = 1;
}
