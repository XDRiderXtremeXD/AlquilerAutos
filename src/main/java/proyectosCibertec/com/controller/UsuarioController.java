package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import proyectosCibertec.com.model.Usuario;
import proyectosCibertec.com.repository.IUsuarioRepository;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private IUsuarioRepository repoUsu;
	
	// Muestra la lista de usuarios
	@GetMapping("/lista")
	public String mostrarLista(Model model) {
		
		model.addAttribute("lstUsuarios", repoUsu.findAll());
		return "usuario/listaUsuario";
	}
	
	// Muestra el formulario de registro
	@GetMapping("/registrar")
	public String abrirRegistrar(Model model) {
		
		model.addAttribute("usuario", new Usuario());
		return "usuario/registrarUsuario";
	}
	
	// Procesa los datos del formulario
	@PostMapping("/grabar")
	public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {
		
			System.out.println("FECHA------- " + usuario.getFecha());
			
			
		try {
			repoUsu.save(usuario);
			model.addAttribute("mensaje", "Usuario registrado exitosamente");
			model.addAttribute("cssmensaje", "alert alert-success");
		} catch (Exception e) {
			model.addAttribute("mensaje", "Error al registrar" + e.getMessage());
			model.addAttribute("cssmensaje", "alert alert-danger");
		}
		
		return "redirect:/usuarios/lista";

	}
}
