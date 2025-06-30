package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyectosCibertec.com.model.Configuracion;
import proyectosCibertec.com.repository.IConfiguracionRepository;
import proyectosCibertec.com.repository.IMonedaRepository;

@Controller
@RequestMapping("/configuracion")
public class ConfiguracionController {
	
	@Autowired
	private IConfiguracionRepository repoConfig;
	@Autowired
	private IMonedaRepository repoMoneda;
	

	@GetMapping("/")
	public String editarConfiguracion(Model model) {

		Configuracion configuracion = repoConfig.findById(1).get();
		model.addAttribute("configuracion", configuracion);
		model.addAttribute("lstMonedas", repoMoneda.findAll());

		return "configuraciones";
	}
	
	@PostMapping("/grabar")
	public String grabar(@ModelAttribute Configuracion configuracion, RedirectAttributes redirAtributos) {
		try {
			repoConfig.save(configuracion);
			redirAtributos.addFlashAttribute("mensaje", "Configuración modificado exitosamente");
			redirAtributos.addFlashAttribute("cssmensaje", "alert alert-success");
		} catch (Exception e) {
			redirAtributos.addFlashAttribute("mensaje", "Error al actualizar: ".concat(e.getMessage()));
			redirAtributos.addFlashAttribute("cssmensaje", "alert alert-danger");
		}
		return "redirect:/configuracion/";
	}
	

}