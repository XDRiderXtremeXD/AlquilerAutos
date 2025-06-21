package proyectosCibertec.com.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
			Vehiculos vehiculo = repoVehiculo.findById(id).orElse(null);
			
			// Borrar imagen de la carpeta
	        if (vehiculo != null) {
	            String nombreFoto = vehiculo.getFoto();
	            
	            if (nombreFoto != null && !nombreFoto.isBlank()) {
	                Path rutaFoto = Paths.get("uploads/img/vehiculos/", nombreFoto);
	                Files.deleteIfExists(rutaFoto);
	            }
	        }
			
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
	    		@RequestParam("fileFoto") MultipartFile file,
	    		RedirectAttributes redirAtributos) {
	        try {
		        // Validación de archivo vacío
		        if (!file.isEmpty()) {
		        		
		            // Validar que sea JPG o JPEG
		            String extension = file.getOriginalFilename().toLowerCase();
		            if (!extension.endsWith(".jpg") && !extension.endsWith(".jpeg")) {
		                redirAtributos.addFlashAttribute("mensaje", "La imagen debe estar en formato JPG o JPEG");
		                redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
		                return "redirect:/vehiculos/listado";
		            }

		            // Validación de tamaño máximo 1MB
		            if (file.getSize() > 1048576) {
		                redirAtributos.addFlashAttribute("mensaje", "La imagen no puede superar 1MB");
		                redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
		                return "redirect:/vehiculos/listado";
		            }
		        }
		        
	        	// Guardar Vehiculo para obtener el ID
	            Vehiculos vehiculoGuardado = repoVehiculo.save(vehiculos);
	            int idGenerado = vehiculoGuardado.getId();
	            
	            // Carpeta de imágenes
	        	String rutaCarpeta = "uploads/img/vehiculos/";
	            Files.createDirectories(Paths.get(rutaCarpeta)); // Se crea en caso no exista
	            
	            // Guardar imagen con nombre ID.jpg
	            String nombreArchivo = idGenerado + ".jpg";
	            Path rutaCompleta = Paths.get(rutaCarpeta + nombreArchivo);
	            Files.createDirectories(rutaCompleta.getParent()); // Crear carpeta si no existe
	            Files.write(rutaCompleta, file.getBytes());
	            
	            // Guardar en la BD
	            vehiculoGuardado.setFoto(nombreArchivo);
	            repoVehiculo.save(vehiculoGuardado);
	            
	            redirAtributos.addFlashAttribute("mensaje", "Vehículo registrado correctamente");
	            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
	        } catch (Exception e) {
	        	redirAtributos.addFlashAttribute("mensaje", "Error al registrar vehículo: " + e.getMessage());
	        	redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
	        }
	        
	        return "redirect:/vehiculos/listado";
	    }
}