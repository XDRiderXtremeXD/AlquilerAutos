package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyectosCibertec.com.model.Moneda;
import proyectosCibertec.com.repository.IMonedaRepository;

@Controller
@RequestMapping("/monedas")
public class MonedaController {

    @Autowired
    private IMonedaRepository repoMoneda;

    @GetMapping("/listado")
    public String monedaCrud(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size) {

        Page<Moneda> lstMonedas = repoMoneda.findAll(PageRequest.of(page, size));
        model.addAttribute("lstMonedas", lstMonedas);
        model.addAttribute("paginaActual", page);
        model.addAttribute("tamanio", size);
        model.addAttribute("moneda", new Moneda());

        return "private-pages/monedas";
    }

    @PostMapping("/grabar")
    public String registrarMoneda(@ModelAttribute Moneda moneda, RedirectAttributes redirAtributos) {
        try {
            repoMoneda.save(moneda);
            redirAtributos.addFlashAttribute("mensaje", "Moneda registrada exitosamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
            redirAtributos.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al registrar: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/monedas/listado";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarMonedas(@PathVariable Integer id, RedirectAttributes redirAtributos) {
        try {
            repoMoneda.deleteById(id);
            redirAtributos.addFlashAttribute("mensaje", "Moneda eliminada correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
            redirAtributos.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al eliminar moneda: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/monedas/listado";
    }
}
