package proyectosCibertec.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import proyectosCibertec.com.model.Documentos;

public interface IDocumentosRepository extends JpaRepository<Documentos, Integer>{
	List<Documentos> findByEstado(int estado);
}
