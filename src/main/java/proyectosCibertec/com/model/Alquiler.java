package proyectosCibertec.com.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Integer numDias;
    private BigDecimal precioDia;
    private BigDecimal abono;
    private LocalDate fechaPrestamo;
    private LocalTime hora;
    private LocalDate fechaDevolucion;
    private Integer idDoc;
    private String observacion;
    private BigDecimal penalidad;
    private BigDecimal penalidad_por_dia;
    private Integer estado;

    // Getters y setters
}