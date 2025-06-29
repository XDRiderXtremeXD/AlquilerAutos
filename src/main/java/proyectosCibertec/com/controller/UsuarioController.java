package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyectosCibertec.com.model.Usuario;
import proyectosCibertec.com.repository.IUsuarioRepository;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IUsuarioRepository repoUsu;

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/listado")
	public String usuarioCrud(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		Page<Usuario> lstUsuarios = repoUsu.findByEstado(1, PageRequest.of(page, size));
		model.addAttribute("lstUsuarios", lstUsuarios);
		model.addAttribute("paginaActual", page);
		model.addAttribute("tamanio", size);
		model.addAttribute("usuario", new Usuario());
		return "usuarios";
	}

	@PostMapping("/grabar")
	public String registrarUsuario(@ModelAttribute Usuario usuario,
			RedirectAttributes redirAtributos) {

		try {
			usuario.setClave(passwordEncoder.encode(usuario.getClave()));
			repoUsu.save(usuario);

			redirAtributos.addFlashAttribute("mensaje", "Usuario registrado exitosamente");
			redirAtributos.addFlashAttribute("cssmensaje", "alert alert-success");
		} catch (Exception e) {
			redirAtributos.addFlashAttribute("mensaje", "Error al registrar" + e.getMessage());
			redirAtributos.addFlashAttribute("cssmensaje", "alert alert-danger");
		}

		return "redirect:/usuarios/listado";

	}

	@PostMapping("/actualizar")
	public String actualizarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirAtributos) {

		try {			
			usuario.setClave(passwordEncoder.encode(usuario.getClave()));
			repoUsu.save(usuario);

			redirAtributos.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
			redirAtributos.addFlashAttribute("cssmensaje", "alert alert-success");
		} catch (Exception e) {
			redirAtributos.addFlashAttribute("mensaje", "Error al registrar" + e.getMessage());
			redirAtributos.addFlashAttribute("cssmensaje", "alert alert-danger");
		}

		return "redirect:/usuarios/listado";

	}

	@GetMapping("/eliminar/{id}")
	public String eliminarUsuario(@PathVariable Integer id, RedirectAttributes addFlashAttribute) {

		try {

			Usuario usuario = repoUsu.findById(id).orElse(null);
			if (usuario != null) {
				usuario.setEstado(0);
				repoUsu.save(usuario);
				addFlashAttribute.addAttribute("mensaje", "Usuario eliminado exitosamente");
				addFlashAttribute.addAttribute("cssmensaje", "alert alert-success");
			}

		} catch (Exception e) {
			addFlashAttribute.addAttribute("mensaje", "Error al registrar" + e.getMessage());
			addFlashAttribute.addAttribute("cssmensaje", "alert alert-danger");
		}

		return "redirect:/usuarios/listado";
	}


}
