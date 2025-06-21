package proyectosCibertec.com.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import proyectosCibertec.com.model.Documentos;
import proyectosCibertec.com.model.Vehiculos;
import proyectosCibertec.com.repository.IDocumentosRepository;

@Controller
@RequestMapping("/documentos")
public class DocumentoController {
	@Autowired
	private IDocumentosRepository repoDocumentos;

	@GetMapping("/listado")
	public String documentos_crud(Model model) {
		List<Documentos> lista = repoDocumentos.findAll();
		model.addAttribute("lstDocumentos", lista);

		model.addAttribute("documentos", new Documentos());

		return "documentos";
	}

	@GetMapping("/editar/{id}")
	public String editarDocumento(Model model, @PathVariable int id) {

		List<Documentos> lista = repoDocumentos.findAll();
		Documentos d = repoDocumentos.findById(id).get();

		model.addAttribute("documentos", d);
		model.addAttribute("lstDocumentos", lista);

		return "documentosEditar";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarDocumento(@PathVariable int id, RedirectAttributes redirAtributos) {
		try {
			Documentos documento = repoDocumentos.findById(id).orElse(null);
			
			// Borrar imagen de la carpeta
	        if (documento != null) {
	            String nombreFoto = documento.getFoto();
	            
	            if (nombreFoto != null && !nombreFoto.isBlank()) {
	                Path rutaFoto = Paths.get("uploads/img/documentos/", nombreFoto);
	                Files.deleteIfExists(rutaFoto);
	            }
	        }
	        
			repoDocumentos.deleteById(id);
			redirAtributos.addFlashAttribute("mensaje", "Documento eliminado correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
        } catch (Exception e) {
        	redirAtributos.addFlashAttribute("mensaje", "Error al eliminar documento: " + e.getMessage());
        	redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
        }
		
		return "redirect:/documentos/listado";
	}

	@PostMapping("/editar")
	public String documentoEditado(@ModelAttribute Documentos documentos, 
			Model model) {
		try {
			repoDocumentos.save(documentos);
			model.addAttribute("mensaje", "Documento actualizado correctamente");
			model.addAttribute("css_mensaje", "alert alert-success");
		} catch (Exception e) {
			model.addAttribute("mensaje", "Error al actualizar documento: " + e.getMessage());
			model.addAttribute("css_mensaje", "alert alert-danger");
		}

		// Cargar lista actualizada
		List<Documentos> lista = repoDocumentos.findAll();
		model.addAttribute("lstDocumentos", lista);
		model.addAttribute("documentos", new Documentos());

		return "documentos";
	}

	@PostMapping("/registro")
	public String registrarDocumento(@ModelAttribute Documentos documentos, 
			@RequestParam("fileFoto") MultipartFile file,
			RedirectAttributes redirAtributos) {
		try {
	        // Validación de archivo vacío
	        if (!file.isEmpty()) {
	        		
	            // Validar que sea JPG o JPEG
	            String extension = file.getOriginalFilename().toLowerCase();
	            if (!extension.endsWith(".jpg") && !extension.endsWith(".jpeg")) {
	                redirAtributos.addFlashAttribute("mensaje", "La imagen debe estar en formato JPG o JPEG");
	                redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
	                return "redirect:/documentos/listado";
	            }

	            // Validación de tamaño máximo 1MB
	            if (file.getSize() > 1048576) {
	                redirAtributos.addFlashAttribute("mensaje", "La imagen no puede superar 1MB");
	                redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
	                return "redirect:/documentos/listado";
	            }
	        }
	        
			// Guardar Documento para obtener el ID
			Documentos docGuardado = repoDocumentos.save(documentos);
			int idGenerado = docGuardado.getId();
			
	        // Carpeta de imágenes
	        String rutaCarpeta = "uploads/img/documentos/";
            Files.createDirectories(Paths.get(rutaCarpeta)); // Se crea en caso no exista
     
            // Guardar imagen con nombre ID.jpg
            String nombreArchivo = idGenerado + ".jpg";
            Path rutaCompleta = Paths.get(rutaCarpeta + nombreArchivo);
            Files.createDirectories(rutaCompleta.getParent()); // Crear carpeta si no existe
            Files.write(rutaCompleta, file.getBytes());
            
	        // Guardar en la BD
            docGuardado.setFoto(nombreArchivo);
			repoDocumentos.save(docGuardado);
			
			redirAtributos.addFlashAttribute("mensaje", "Documento registrado correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
        } catch (Exception e) {
        	redirAtributos.addFlashAttribute("mensaje", "Error al registrar documento: " + e.getMessage());
        	redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
        }
		
		return "redirect:/documentos/listado";
	}
}
