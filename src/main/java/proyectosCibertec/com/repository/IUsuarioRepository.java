package proyectosCibertec.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import proyectosCibertec.com.model.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

	Usuario findByNomUsuario(String nomUsuario);
	Usuario findByNomUsuarioAndClave(String nomUsuario, String clave);
}
