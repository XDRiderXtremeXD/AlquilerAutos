package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import proyectosCibertec.com.model.Usuario;
import proyectosCibertec.com.repository.IUsuarioRepository;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private IUsuarioRepository repoUsu;

	@GetMapping("/listado")
	public String listarUsuarios(Model model) {

		model.addAttribute("lstUsuarios", repoUsu.findAll());
		model.addAttribute("usuario", new Usuario());
		return "usuario/usuarios";
	}

	@PostMapping("/grabar")

	public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {

		try {
			repoUsu.save(usuario);
			model.addAttribute("mensaje", "Usuario registrado exitosamente");
			model.addAttribute("cssmensaje", "alert alert-success");
		} catch (Exception e) {
			model.addAttribute("mensaje", "Error al registrar" + e.getMessage());
			model.addAttribute("cssmensaje", "alert alert-danger");
		}

		return "redirect:/usuarios/listado";

	}

	@GetMapping("/editar/{id}")
	public String mostrarEdicion(@PathVariable int id, Model model) {

		Usuario usuario = repoUsu.findById(id).get();
		model.addAttribute("usuario", usuario);

		return "usuario/editarUsuario";
	}

	@PostMapping("/actualizar")
	public String actualizar(@ModelAttribute Usuario usuario, Model model) {
		try {
			repoUsu.save(usuario);
			model.addAttribute("mensaje", "Usuario actualizado exitosamente");
			model.addAttribute("cssmensaje", "alert alert-success");
		} catch (Exception e) {
			model.addAttribute("mensaje", "Error al actualizar: ".concat(e.getMessage()));
			model.addAttribute("cssmensaje", "alert alert-danger");
		}
		return "redirect:/usuarios/listado";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarUsuario(@PathVariable Integer id) {

		repoUsu.deleteById(id);
		return "redirect:/usuarios/listado";
	}

}
