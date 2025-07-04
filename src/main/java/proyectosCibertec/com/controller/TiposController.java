package proyectosCibertec.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        List<Tipos> listaTipos = repoTipo.findByEstado(1); // Solo activos
        model.addAttribute("lstTipos", listaTipos);
        model.addAttribute("tipos", new Tipos());

        // Para el listado de activos
        model.addAttribute("vista", "activos");

        return "private-pages/tipos";
    }

    @PostMapping("/editar")
    public String editarTipo(@ModelAttribute Tipos tipo, RedirectAttributes redirAtributos) {
        try {
            repoTipo.save(tipo);
            redirAtributos.addFlashAttribute("mensaje", "Tipo actualizado correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
            redirAtributos.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al actualizar tipo: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/tipos/listado";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarTipo(@PathVariable int id, RedirectAttributes redirAtributos) {
        try {
            Tipos tipo = repoTipo.findById(id).get();
            tipo.setEstado(2);
            repoTipo.save(tipo);

            redirAtributos.addFlashAttribute("mensaje", "Tipo eliminado correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
            redirAtributos.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al eliminar tipo: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/tipos/listado";
    }

    @PostMapping("/registro")
    public String registrarMarca(@ModelAttribute Tipos tipo, RedirectAttributes redirAtributos) {
        try {
            repoTipo.save(tipo);
            redirAtributos.addFlashAttribute("mensaje", "Tipo registrado correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
            redirAtributos.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al registrar tipo: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/tipos/listado";
    }

    @GetMapping("/cancelados")
    public String tiposCancelados(Model model) {
        List<Tipos> listaCancelados = repoTipo.findByEstado(2);
        model.addAttribute("lstTipos", listaCancelados);
        model.addAttribute("tipos", new Tipos());
        model.addAttribute("vista", "cancelados");
        return "tipos";
    }

    @GetMapping("/restaurar/{id}")
    public String restaurarTipo(@PathVariable int id, RedirectAttributes redirAtributos) {
        try {
            Tipos tipo = repoTipo.findById(id).orElse(null);
            if (tipo != null) {
                tipo.setEstado(1);
                repoTipo.save(tipo);
                redirAtributos.addFlashAttribute("mensaje", "Tipo restaurado correctamente");
                redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
                redirAtributos.addFlashAttribute("tipoMensaje", "success");
            }
        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al restaurar tipo: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/tipos/cancelados";
    }
}
