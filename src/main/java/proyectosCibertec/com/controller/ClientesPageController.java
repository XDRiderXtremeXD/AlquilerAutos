package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import proyectosCibertec.com.model.Configuracion;
import proyectosCibertec.com.repository.IConfiguracionRepository;

@Controller
public class ClientesPageController {

	@Autowired
    private IConfiguracionRepository repoConfig;
	
	@GetMapping("/contactos")
    public String contactosVista(Model model) {
		Configuracion configuracion = repoConfig.findById(1).orElseThrow();
	    model.addAttribute("configuracion", configuracion);
        return "contentclient/contactos";
    }
	
	@GetMapping("/catalogo")
    public String catalogoVista() {
        return "contentclient/catalogo";
    }
	
	@GetMapping("/quienessomos")
    public String quienessomosVista() {
        return "contentclient/quienes-somos";
    }
	
	@GetMapping("/home")
    public String homeVista() {
        return "contentclient/home";
    }
	
	@GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }
}

