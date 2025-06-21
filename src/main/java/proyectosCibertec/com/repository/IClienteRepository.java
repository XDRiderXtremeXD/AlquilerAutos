package proyectosCibertec.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import proyectosCibertec.com.model.Clientes;

public interface IClienteRepository extends JpaRepository<Clientes, Integer> {

}
