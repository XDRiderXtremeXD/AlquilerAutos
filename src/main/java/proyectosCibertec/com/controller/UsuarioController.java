package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import proyectosCibertec.com.model.Usuario;
import proyectosCibertec.com.repository.IUsuarioRepository;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private IUsuarioRepository repoUsu;
	
	@GetMapping("/lista")
	public String lista(Model model) {
		
		model.addAttribute("lstUsuarios", repoUsu.findAll());
		model.addAttribute("usuario", new Usuario());
		
		return "usuario/listaUsuario";
	}
	
	

}
