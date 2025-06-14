package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import proyectosCibertec.com.model.Alquiler;
import proyectosCibertec.com.repository.IAlquilerRepository;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/alquiler")
public class AlquilerController {

	@Autowired
	private IAlquilerRepository alquilerRepository;

	@GetMapping("/index")
	public String alquilerVista(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		Page<Alquiler> pagina = alquilerRepository.findAll(PageRequest.of(page, size));
		model.addAttribute("pagina", pagina);
		model.addAttribute("paginaActual", page);
		model.addAttribute("tamanio", size);

		return "indexAlquiler";
	}
}
