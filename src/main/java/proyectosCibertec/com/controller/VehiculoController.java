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
import proyectosCibertec.com.model.Tipos;
import proyectosCibertec.com.model.Vehiculos;
import proyectosCibertec.com.repository.IMarcasRepository;
import proyectosCibertec.com.repository.ITiposRepository;
import proyectosCibertec.com.repository.IVehiculosRepository;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {
    @Autowired
    private IVehiculosRepository repoVehiculo;

    @Autowired
    private IMarcasRepository repoMarca;

    @Autowired
    private ITiposRepository repoTipo;

    @GetMapping("/listado")
    public String vehiculos_crud(Model model) {
    	List<Vehiculos> listaVehiculos = repoVehiculo.findAll();
        List<Marcas> 	listaMarcas    = repoMarca.findAll();
        List<Tipos> 	listaTipos 	   = repoTipo.findAll();
      
        model.addAttribute("lstVehiculos", listaVehiculos);
        model.addAttribute("lstMarcas",    listaMarcas);
        model.addAttribute("lstTipos", 	   listaTipos);

        model.addAttribute("vehiculos", new Vehiculos());
      
        return "vehiculos";
    }
    
	@GetMapping("/editar/{id}")
	public String editarVehiculo(Model model, @PathVariable int id) {

		List<Vehiculos> lista = repoVehiculo.findAll();
		Vehiculos v = repoVehiculo.findById(id).get();

		model.addAttribute("vehiculos", v);
		model.addAttribute("lstDocumentos", lista);

		return "redirect:/vehiculos/listado";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarVehiculo(@PathVariable int id, RedirectAttributes redirAtributos) {
		try {
			repoVehiculo.deleteById(id);
			redirAtributos.addFlashAttribute("mensaje", "Vehículo eliminado correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
        } catch (Exception e) {
        	redirAtributos.addFlashAttribute("mensaje", "Error al eliminar vehículo: " + e.getMessage());
        	redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
        }

		return "redirect:/vehiculos/listado";
	}
    
	    @PostMapping("/registro")
	    public String registrarVehiculo(@ModelAttribute Vehiculos vehiculos, 
	    		RedirectAttributes redirAtributos) {
	        try {
	            repoVehiculo.save(vehiculos);
	            redirAtributos.addFlashAttribute("mensaje", "Vehículo registrado correctamente");
	            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
	        } catch (Exception e) {
	        	redirAtributos.addFlashAttribute("mensaje", "Error al registrar vehículo: " + e.getMessage());
	        	redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
	        }
	        
	        return "redirect:/vehiculos/listado";
	    }
}