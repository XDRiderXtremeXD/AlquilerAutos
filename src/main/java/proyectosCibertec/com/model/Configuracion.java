package proyectosCibertec.com.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "configuracion")
@Data
public class Configuracion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String ruc;
	private String nombre;
	private String telefono;
	private String correo;
	private String direccion;
	private String mensaje;
	private String logo;
	@Column(name = "moneda")
	private Integer idmoneda;
	private Integer impuesto;
	@Column(name = "cant_factura")
    private Integer cantidadFactura;
	@Column(name = "penalidad_por_dia")
    private double penalidadPorDia = 0;
	
	@ManyToOne
	@JoinColumn(name = "moneda", insertable = false, updatable = false)
	private Moneda moneda;
}
