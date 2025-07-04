package proyectosCibertec.com.controller;

import java.util.HashMap;
import java.io.OutputStream;
import javax.sql.DataSource;

import org.springframework.core.io.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyectosCibertec.com.model.Usuario;
import proyectosCibertec.com.repository.IUsuarioRepository;
import proyectosCibertec.com.service.CloudinaryService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private CloudinaryService cloudinaryService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IUsuarioRepository repoUsu;
	
	@Autowired
	private DataSource dataSource; // javax.sql

	@Autowired
	private ResourceLoader resourceLoader; // core.io

	@GetMapping("/listado")
	public String usuarioCrud(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		Page<Usuario> lstUsuarios = repoUsu.findByEstado(1, PageRequest.of(page, size));
		model.addAttribute("lstUsuarios", lstUsuarios);
		model.addAttribute("paginaActual", page);
		model.addAttribute("tamanio", size);
		model.addAttribute("usuario", new Usuario());

		return "private-pages/usuarios";
	}

	@PostMapping("/grabar")
	public String registrarUsuario(@ModelAttribute Usuario usuario, @RequestParam("filePerfil") MultipartFile file,
			RedirectAttributes redirAtributos) {
		try {
			if (file.isEmpty()) {
				redirAtributos.addFlashAttribute("mensaje", "Debe seleccionar una imagen de perfil para el usuario.");
				redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
				redirAtributos.addFlashAttribute("tipoMensaje", "error");
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
				redirAtributos.addFlashAttribute("tipoMensaje", "error");
				return "redirect:/usuarios/listado";
			}

			// Validación de tamaño
			if (file.getSize() > 1048576) {
				redirAtributos.addFlashAttribute("mensaje", "La imagen no puede superar 1MB");
				redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
				redirAtributos.addFlashAttribute("tipoMensaje", "error");
				return "redirect:/usuarios/listado";
			}

			// Subir a Cloudinary
			String secureUrl = cloudinaryService.subirImagen(file, "usuarios");
			usuario.setPerfil(secureUrl);

			// Encriptar contraseña
			usuario.setClave(passwordEncoder.encode(usuario.getClave()));

			// Guardar usuario
			repoUsu.save(usuario);

			redirAtributos.addFlashAttribute("mensaje", "Usuario registrado exitosamente");
			redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
			redirAtributos.addFlashAttribute("tipoMensaje", "success");

		} catch (Exception e) {
			redirAtributos.addFlashAttribute("mensaje", "Error al registrar usuario: " + e.getMessage());
			redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
			redirAtributos.addFlashAttribute("tipoMensaje", "error");
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

	@PostMapping("/eliminar/{id}")
	public String eliminarUsuario(@PathVariable Integer id, RedirectAttributes redirAtributos) {
		try {
			Usuario usuario = repoUsu.findById(id).orElse(null);
			if (usuario != null) {
				usuario.setEstado(0);
				repoUsu.save(usuario);
				redirAtributos.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
				redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
				redirAtributos.addFlashAttribute("tipoMensaje", "success");
			}
		} catch (Exception e) {
			redirAtributos.addFlashAttribute("mensaje", "Error al eliminar usuario: " + e.getMessage());
			redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
			redirAtributos.addFlashAttribute("tipoMensaje", "error");
		}
		return "redirect:/usuarios/listado";
	}

	@GetMapping("/reporte")
	public void generarReporte(HttpServletResponse response) {
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=usuarios.pdf");

		try {
			String ruta = resourceLoader.getResource("classpath:/static/reports/usuarios.jasper").getFile()
					.getAbsolutePath();

			JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, new HashMap<>(), dataSource.getConnection());

			OutputStream outStream = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
