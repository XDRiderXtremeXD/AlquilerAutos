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
@Table(name = "moneda")
@Data
public class Moneda {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String simbolo;
	private String nombre;
	@Column(nullable = false)
	private LocalDateTime fecha = LocalDateTime.now();
	private Integer estado = 1;
}
