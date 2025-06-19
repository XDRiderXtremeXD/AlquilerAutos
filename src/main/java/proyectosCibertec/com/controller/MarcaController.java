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

import proyectosCibertec.com.model.Marcas;
import proyectosCibertec.com.repository.IMarcasRepository;

@Controller
@RequestMapping("/marcas")
public class MarcaController {
	@Autowired
	private IMarcasRepository repoMarca;
	
	@GetMapping("/listado")
    public String marcas_crud(Model model) {
        List<Marcas> 	listaMarcas    = repoMarca.findAll();
        model.addAttribute("lstMarcas",    listaMarcas);

        model.addAttribute("marcas", new Marcas());
      
        return "marcas";
    }
	
	@GetMapping("/editar/{id}")
	public String editarMarca(Model model, @PathVariable int id) {

		List<Marcas> lista = repoMarca.findAll();
		Marcas m = repoMarca.findById(id).get();

		model.addAttribute("marcas", m);
		model.addAttribute("lstMarcas", lista);

		return "redirect:/marcas/listado";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminarMarca(@PathVariable int id, RedirectAttributes redirAtributos) {
		try {
			repoMarca.deleteById(id);
			redirAtributos.addFlashAttribute("mensaje", "Marca eliminada correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
        } catch (Exception e) {
        	redirAtributos.addFlashAttribute("mensaje", "Error al eliminar marca: " + e.getMessage());
        	redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
        }

		return "redirect:/marcas/listado";
	}
	
    @PostMapping("/registro")
    public String registrarMarca(@ModelAttribute Marcas marca, 
    		RedirectAttributes redirAtributos) {
        try {
            repoMarca.save(marca);
            redirAtributos.addFlashAttribute("mensaje", "Marca registrada correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
        } catch (Exception e) {
        	redirAtributos.addFlashAttribute("mensaje", "Error al registrar marca: " + e.getMessage());
        	redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
        }
        
        return "redirect:/marcas/listado";
    }
}
