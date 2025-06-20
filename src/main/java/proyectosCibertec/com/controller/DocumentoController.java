package proyectosCibertec.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyectosCibertec.com.model.Documentos;
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
			RedirectAttributes redirAtributos) {
		try {
			repoDocumentos.save(documentos);
			redirAtributos.addFlashAttribute("mensaje", "Documento registrado correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
        } catch (Exception e) {
        	redirAtributos.addFlashAttribute("mensaje", "Error al registrar documento: " + e.getMessage());
        	redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
        }
		
		return "redirect:/documentos/listado";
	}
}
