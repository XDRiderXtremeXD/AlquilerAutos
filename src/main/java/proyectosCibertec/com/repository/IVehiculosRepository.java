package proyectosCibertec.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import proyectosCibertec.com.model.Vehiculos;

public interface IVehiculosRepository extends JpaRepository<Vehiculos, Integer> {
	List<Vehiculos> findByIdMarcaAndEstado(int marca, int estado);
	List<Vehiculos> findByEstado(int estado);
	List<Vehiculos> findByActividad(String actividad);
	List<Vehiculos> findByIdMarcaAndEstadoAndActividad(int marca, int estado,String actividad);
}