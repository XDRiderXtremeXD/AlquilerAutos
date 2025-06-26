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
@Data
@Table(name = "vehiculos")
public class Vehiculos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;	
    @Column(name = "id_marca")
    private int idMarca;
    @Column(name = "id_tipo")
    private int idTipo;
    private String placa;
    private String modelo;
    private String foto;
    private String actividad;
    private int estado = 1;
    @Column(name = "precio_por_dia")
    private double precioXDia;

    @ManyToOne
    @JoinColumn(name = "id_marca", insertable = false, updatable = false)
    private Marcas objMarca;

    @ManyToOne
    @JoinColumn(name = "id_tipo", insertable = false, updatable = false)
    private Tipos objTipo;
}