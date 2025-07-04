package proyectosCibertec.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import proyectosCibertec.com.model.Usuario;
import proyectosCibertec.com.repository.IUsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Controller
public class PerfilUsuarioController {
	
	@Autowired
	private IUsuarioRepository repoUsu;
	
	@GetMapping("/usuario/perfil")
	public String detalleUsuario(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String nombreUsuario = auth.getName();
	    Usuario usuario = repoUsu.findByNomUsuario(nombreUsuario);

	    model.addAttribute("usuario", usuario);
	    return "private-pages/perfil";
	}
	
}
