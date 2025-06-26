package proyectosCibertec.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import proyectosCibertec.com.model.Tipos;

public interface ITiposRepository extends JpaRepository<Tipos, Integer> {
	List<Tipos> findByEstado(int estado);
}

