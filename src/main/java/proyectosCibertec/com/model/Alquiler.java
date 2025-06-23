package proyectosCibertec.com.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

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
@Data
@Table(name="alquiler")
public class Alquiler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer idCliente;
    private Integer idVehiculo;
    private Integer idMoneda;
    private Integer numDias;
    private BigDecimal precioDia;
    private BigDecimal abono;
    private LocalDate fechaPrestamo;
    private LocalTime hora;
    private LocalDate fechaEstimadaDevolucion;
    private LocalDate fechaRealDevolucion;
    private Integer idDoc;
    private String observacion;
    private Integer estado;
    private BigDecimal penalidad;
    @Column(name = "penalidad_por_dia")
    private double penalidadPorDia;

    @ManyToOne
	@JoinColumn(name = "idCliente", insertable = false, updatable = false)
	private Clientes cliente;
    
    @ManyToOne
	@JoinColumn(name = "idVehiculo", insertable = false, updatable = false)
	private Vehiculos vehiculo;
    
    @ManyToOne
	@JoinColumn(name = "idDoc", insertable = false, updatable = false)
	private Documentos documento;
    
    @ManyToOne
	@JoinColumn(name = "idMoneda", insertable = false, updatable = false)
	private Moneda moneda;
    // Getters y setters
}