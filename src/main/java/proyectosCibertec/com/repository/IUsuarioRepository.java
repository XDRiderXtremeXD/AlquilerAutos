package proyectosCibertec.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import proyectosCibertec.com.model.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

}
