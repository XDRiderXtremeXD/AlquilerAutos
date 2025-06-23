package proyectosCibertec.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import proyectosCibertec.com.model.Vehiculos;

public interface IVehiculosRepository extends JpaRepository<Vehiculos, Integer> {
	
	List<Vehiculos> findByIdMarcaAndEstado(int id_marca, int estado);
}