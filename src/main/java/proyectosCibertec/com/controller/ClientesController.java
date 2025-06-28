package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import proyectosCibertec.com.model.Clientes;
import proyectosCibertec.com.repository.IClienteRepository;

@Controller
@RequestMapping("/clientes")
public class ClientesController {

    @Autowired
    private IClienteRepository repoCliente;

    @GetMapping("/listado")
    public String listarClientes(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Clientes> lstClientes = repoCliente.findByEstado(1, PageRequest.of(page, size));
        model.addAttribute("lstClientes", lstClientes);
        model.addAttribute("paginaActual", page);
        model.addAttribute("tamanio", size);
        model.addAttribute("cliente", new Clientes()); // para el formulario modal

        return "clientes";
    }

    @PostMapping("/grabar")
    public String registrarCliente(@ModelAttribute Clientes cliente, Model model) {
        try {
            cliente.setEstado(1); // aseguramos que se registre como activo
            repoCliente.save(cliente);
            model.addAttribute("mensaje", "Cliente registrado exitosamente");
            model.addAttribute("cssmensaje", "alert alert-success");
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al registrar: " + e.getMessage());
            model.addAttribute("cssmensaje", "alert alert-danger");
        }

        return "redirect:/clientes/listado";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Integer id, Model model) {
        Clientes cliente = repoCliente.findById(id).orElse(null);

        if (cliente != null) {
            try {
                cliente.setEstado(0); // eliminación lógica
                repoCliente.save(cliente);
                model.addAttribute("mensaje", "Cliente eliminado exitosamente");
                model.addAttribute("cssmensaje", "alert alert-success");
            } catch (Exception e) {
                model.addAttribute("mensaje", "Error al eliminar: " + e.getMessage());
                model.addAttribute("cssmensaje", "alert alert-danger");
            }
        }

        return "redirect:/clientes/listado";
    }
}
