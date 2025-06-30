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
  
	public String registrarUsuario(
	        @ModelAttribute Usuario usuario,
	        @RequestParam("filePerfil") MultipartFile file, // este nombre debe coincidir con el del formulario
	        RedirectAttributes redirAtributos) {

	    try {
	        // Validación de archivo vacío
	        if (file.isEmpty()) {
	            redirAtributos.addFlashAttribute("mensaje", "Debe seleccionar una imagen de perfil para el usuario.");
	            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
	            return "redirect:/usuarios/listado";
	        }

	        // Validar extensión
	        String originalFilename = file.getOriginalFilename();
	        String extension = "";
	        if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
	            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
	        }
	        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
	            redirAtributos.addFlashAttribute("mensaje", "La imagen debe estar en formato JPG, JPEG o PNG");
	            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
	            return "redirect:/usuarios/listado";
	        }

	        // Validación de tamaño
	        if (file.getSize() > 1048576) {
	            redirAtributos.addFlashAttribute("mensaje", "La imagen no puede superar 1MB");
	            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
	            return "redirect:/usuarios/listado";
	        }

	        // Subir a Cloudinary
	        String secureUrl = cloudinaryService.subirImagen(file, "usuarios");
	        usuario.setPerfil(secureUrl); // guardar la URL en el campo perfil

	        // Encriptar contraseña
	        usuario.setClave(passwordEncoder.encode(usuario.getClave()));

	        // Guardar en BD
	        repoUsu.save(usuario);

	        redirAtributos.addFlashAttribute("mensaje", "Usuario registrado exitosamente");
	        redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");

	    } catch (Exception e) {
	        redirAtributos.addFlashAttribute("mensaje", "Error al registrar usuario: " + e.getMessage());
	        redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
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
