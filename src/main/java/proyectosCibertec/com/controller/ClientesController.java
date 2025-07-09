package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyectosCibertec.com.model.Clientes;
import proyectosCibertec.com.repository.IClienteRepository;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/clientes")
public class ClientesController {

    @Autowired
    private IClienteRepository repoCliente;
    
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourceLoader resourceLoader;

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

        return "private-pages/clientes";
    }

    @PostMapping("/grabar")
    public String registrarCliente(@ModelAttribute Clientes cliente, RedirectAttributes redirAtributos) {
        try {
            cliente.setEstado(1); // aseguramos que se registre como activo
            repoCliente.save(cliente);
            redirAtributos.addFlashAttribute("mensaje", "Cliente registrado exitosamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
            redirAtributos.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al registrar: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/clientes/listado";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Integer id, RedirectAttributes redirAtributos) {
        Clientes cliente = repoCliente.findById(id).orElse(null);

        if (cliente != null) {
            try {
                cliente.setEstado(0); // eliminación lógica
                repoCliente.save(cliente);
                redirAtributos.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
                redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
                redirAtributos.addFlashAttribute("tipoMensaje", "success");
            } catch (Exception e) {
                redirAtributos.addFlashAttribute("mensaje", "Error al eliminar: " + e.getMessage());
                redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
                redirAtributos.addFlashAttribute("tipoMensaje", "error");
            }
        } else {
            redirAtributos.addFlashAttribute("mensaje", "Cliente no encontrado");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-warning");
            redirAtributos.addFlashAttribute("tipoMensaje", "warning");
        }

        return "redirect:/clientes/listado";
    }
    
    @GetMapping("/reporte")
    public void generarReporteClientes(HttpServletResponse response) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=clientes.pdf");

        try {
            // Ruta del archivo .jasper compilado
            String ruta = resourceLoader.getResource("classpath:/static/reports/cliente.jasper")
                                        .getFile()
                                        .getAbsolutePath();

            Map<String, Object> parametros = new HashMap<>();
            // Agrega parámetros si los necesitas: parametros.put("clave", valor);

            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, dataSource.getConnection());

            OutputStream out = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
