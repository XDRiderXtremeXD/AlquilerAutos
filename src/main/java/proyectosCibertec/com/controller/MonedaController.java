package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyectosCibertec.com.model.Moneda;
import proyectosCibertec.com.repository.IMonedaRepository;

@Controller
@RequestMapping("/monedas")
public class MonedaController {
	
	@Autowired
	private IMonedaRepository repoMoneda;
	
	@GetMapping("/listado")
	public String monedaCrud(Model model,  @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		
		Page<Moneda> lstMonedas = repoMoneda.findAll(PageRequest.of(page, size));
		model.addAttribute("lstMonedas", lstMonedas);
		model.addAttribute("paginaActual", page);
		model.addAttribute("tamanio", size);
		model.addAttribute("moneda", new Moneda());
		
		return "monedas";
	}
	
	
	@PostMapping("/grabar")
	public String registrarMoneda(@ModelAttribute Moneda moneda, Model model) {

		try {
			repoMoneda.save(moneda);
			model.addAttribute("mensaje", "Moneda registrado exitosamente");
			model.addAttribute("cssmensaje", "alert alert-success");
		} catch (Exception e) {
			model.addAttribute("mensaje", "Error al registrar" + e.getMessage());
			model.addAttribute("cssmensaje", "alert alert-danger");
		}
		return "redirect:/monedas/listado";

	}

	
	@GetMapping("/eliminar/{id}")
	public String eliminarMonedas(@PathVariable Integer id) {

		repoMoneda.deleteById(id);
		return "redirect:/monedas/listado";
	}
	
}
