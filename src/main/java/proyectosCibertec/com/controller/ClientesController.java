package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import proyectosCibertec.com.model.Clientes;
import proyectosCibertec.com.repository.IClienteRepository;

@Controller
@RequestMapping("/clientes")
public class ClientesController {

        @Autowired
        private IClienteRepository repoCli;
        
        // Listado de clientes
        @GetMapping("/listado")
        
        public String listaClientes(Model model) {
        	model.addAttribute("lstClientes", repoCli.findAll());
        	model.addAttribute("clientes", new Clientes());
        	return "clientes";
        }
        
        @GetMapping("/grabar")
        
        public String registrarCliente(@ModelAttribute Clientes clientes, Model model) {
        	try {
				repoCli.save(clientes);
				model.addAttribute("mensaje", "Cliente registrado exitosamente");
				model.addAttribute("cssmensaje", "alert alert-success");
			} catch (Exception e) {
				model.addAttribute("mensaje", "Error al registrarse"+e.getMessage());
				model.addAttribute("cssmensaje", "alert alert-danger");
			}
        	return "redirect:/clientes/listado";
        }
        
        @GetMapping("/actualizar")
        
        public String actualizarCliente(@ModelAttribute Clientes clientes, Model model) {
        	try {
        		repoCli.save(clientes);
        		model.addAttribute("mensaje", "Clientes actualizado exitosamente");
                model.addAttribute("cssmensaje", "alert alert-success");
				
			} catch (Exception e) {
				model.addAttribute("mensaje", "Error al actualizar: ".concat(e.getMessage()));
		        model.addAttribute("cssmensaje", "alert alert-danger");
			}
        	
        	 return "redirect:/clientes/listado";
        }
        
        @GetMapping("/eliminar")
        
        public String EliminarCliente(@ModelAttribute Clientes clientes, Model model) {
        	try {
                repoCli.deleteById(clientes.getId());
                model.addAttribute("mensaje", "Cliente eliminado exitosamente");
                model.addAttribute("cssmensaje", "alert alert-success");
            } catch (Exception e) {
                model.addAttribute("mensaje", "Error al eliminar: " + e.getMessage());
                model.addAttribute("cssmensaje", "alert alert-danger");
            }
            return "redirect:/clientes/listado";
        }
	  
        
}
