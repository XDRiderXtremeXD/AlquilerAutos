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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyectosCibertec.com.model.Usuario;
import proyectosCibertec.com.repository.IUsuarioRepository;
import proyectosCibertec.com.service.CloudinaryService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private CloudinaryService cloudinaryService;

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
	public String registrarUsuario( @ModelAttribute Usuario usuario,
	        @RequestParam("imagenPerfil") MultipartFile imagenPerfil,
	        RedirectAttributes redirAtributos) {
	    try {
	        if (!imagenPerfil.isEmpty()) {
	            String urlImagen = cloudinaryService.subirImagen(imagenPerfil, "usuarios");
	            usuario.setPerfil(urlImagen); // Guarda la URL en tu entidad
	        }
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
	public String actualizarUsuario(@ModelAttribute Usuario usuario,
	        @RequestParam(value = "imagenPerfil", required = false) MultipartFile imagenPerfil,
	        RedirectAttributes redirAtributos) {
	    try {
	        if (imagenPerfil != null && !imagenPerfil.isEmpty()) {
	            String urlImagen = cloudinaryService.subirImagen(imagenPerfil, "usuarios");
	            usuario.setPerfil(urlImagen); // Actualiza la URL en tu entidad
	        }
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

	private String extractPublicId(String imageUrl) {
		if (imageUrl == null || imageUrl.isEmpty())
			return null;
		String[] parts = imageUrl.split("/");
		String publicIdWithExtension = parts[parts.length - 1];
		return publicIdWithExtension.split("\\.")[0];
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
