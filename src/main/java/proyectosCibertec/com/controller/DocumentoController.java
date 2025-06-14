package proyectosCibertec.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		
		return "documentos";
	}
}
