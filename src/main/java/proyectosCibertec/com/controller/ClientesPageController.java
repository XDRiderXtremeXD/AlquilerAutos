package proyectosCibertec.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientesPageController {

	@GetMapping("/contactos")
    public String contactosVista() {
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

