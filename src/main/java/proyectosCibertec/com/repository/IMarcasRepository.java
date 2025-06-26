package proyectosCibertec.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import proyectosCibertec.com.model.Marcas;


public interface IMarcasRepository extends JpaRepository<Marcas, Integer> {
	List<Marcas> findByEstado(int estado);
}

