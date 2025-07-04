package proyectosCibertec.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        List<Marcas> listaMarcas = repoMarca.findByEstado(1);
        model.addAttribute("lstMarcas", listaMarcas);
        model.addAttribute("marcas", new Marcas());
        model.addAttribute("vista", "activas");
        return "private-pages/marcas";
    }

    @PostMapping("/editar")
    public String editarMarca(@ModelAttribute Marcas marca, RedirectAttributes redirAtributos) {
        try {
            repoMarca.save(marca);
            redirAtributos.addFlashAttribute("mensaje", "Marca actualizada correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
            redirAtributos.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al actualizar marca: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/marcas/listado";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarMarca(@PathVariable int id, RedirectAttributes redirAtributos) {
        try {
            Marcas m = repoMarca.findById(id).orElseThrow();
            m.setEstado(2);
            repoMarca.save(m);
            redirAtributos.addFlashAttribute("mensaje", "Marca eliminada correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
            redirAtributos.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al eliminar marca: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/marcas/listado";
    }

    @PostMapping("/registro")
    public String registrarMarca(@ModelAttribute Marcas marca, RedirectAttributes redirAtributos) {
        try {
            repoMarca.save(marca);
            redirAtributos.addFlashAttribute("mensaje", "Marca registrada correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
            redirAtributos.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al registrar marca: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/marcas/listado";
    }

    @GetMapping("/canceladas")
    public String marcasCanceladas(Model model) {
        List<Marcas> listaCanceladas = repoMarca.findByEstado(2);
        model.addAttribute("lstMarcas", listaCanceladas);
        model.addAttribute("marcas", new Marcas());
        model.addAttribute("vista", "canceladas");
        return "private-pages/marcas";
    }

    @GetMapping("/restaurar/{id}")
    public String restaurarMarca(@PathVariable int id, RedirectAttributes redirAtributos) {
        try {
            Marcas marca = repoMarca.findById(id).orElseThrow();
            marca.setEstado(1);
            repoMarca.save(marca);
            redirAtributos.addFlashAttribute("mensaje", "Marca restaurada correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
            redirAtributos.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al restaurar marca: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/marcas/canceladas";
    }
}
