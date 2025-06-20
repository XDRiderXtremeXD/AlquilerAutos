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

import proyectosCibertec.com.model.Tipos;
import proyectosCibertec.com.repository.ITiposRepository;

@Controller
@RequestMapping("/tipos")
public class TiposController {
	@Autowired
	private ITiposRepository repoTipo;
	
	@GetMapping("/listado")
    public String tipos_crud(Model model) {
        List<Tipos> listaTipos = repoTipo.findAll();
        model.addAttribute("lstTipos", listaTipos);

        model.addAttribute("tipos", new Tipos());
      
        return "tipos";
    }
	
	@GetMapping("/editar/{id}")
	public String editarTipo(Model model, @PathVariable int id) {

		List<Tipos> lista = repoTipo.findAll();
		Tipos t = repoTipo.findById(id).get();

		model.addAttribute("tipos", t);
		model.addAttribute("lstTipos", lista);

		return "redirect:/tipos/listado";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminarTipo(@PathVariable int id, RedirectAttributes redirAtributos) {
		try {
			repoTipo.deleteById(id);
			redirAtributos.addFlashAttribute("mensaje", "Tipo eliminado correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
        } catch (Exception e) {
        	redirAtributos.addFlashAttribute("mensaje", "Error al eliminar tipo: " + e.getMessage());
        	redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
        }

		return "redirect:/tipos/listado";
	}
	
    @PostMapping("/registro")
    public String registrarMarca(@ModelAttribute Tipos tipo, 
    		RedirectAttributes redirAtributos) {
        try {
        	repoTipo.save(tipo);
            redirAtributos.addFlashAttribute("mensaje", "Tipo registrado correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
        } catch (Exception e) {
        	redirAtributos.addFlashAttribute("mensaje", "Error al registrar tipo: " + e.getMessage());
        	redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
        }
        
        return "redirect:/tipos/listado";
    }
}
