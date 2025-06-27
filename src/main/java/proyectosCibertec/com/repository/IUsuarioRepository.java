package proyectosCibertec.com.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import proyectosCibertec.com.model.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

	Usuario findByNomUsuario(String nomUsuario);
	Page<Usuario> findByEstado(Integer estado, PageRequest pageRequest);
}
