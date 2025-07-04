package proyectosCibertec.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyectosCibertec.com.model.Marcas;
import proyectosCibertec.com.model.Tipos;
import proyectosCibertec.com.model.Vehiculos;
import proyectosCibertec.com.repository.IMarcasRepository;
import proyectosCibertec.com.repository.ITiposRepository;
import proyectosCibertec.com.repository.IVehiculosRepository;
import proyectosCibertec.com.service.CloudinaryService;

import java.io.OutputStream;
import java.util.HashMap;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.core.io.ResourceLoader;
import javax.sql.DataSource;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private IVehiculosRepository repoVehiculo;

    @Autowired
    private IMarcasRepository repoMarca;

    @Autowired
    private ITiposRepository repoTipo;

    @Autowired
    private CloudinaryService cloudinaryService;
    
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/listado")
    public String vehiculos_crud(Model model) {
        List<Vehiculos> listaVehiculos = repoVehiculo.findByEstado(1);
        List<Marcas> listaMarcas = repoMarca.findByEstado(1);
        List<Tipos> listaTipos = repoTipo.findByEstado(1);

        model.addAttribute("lstVehiculos", listaVehiculos);
        model.addAttribute("lstMarcas", listaMarcas);
        model.addAttribute("lstTipos", listaTipos);
        model.addAttribute("vehiculos", new Vehiculos());
        model.addAttribute("vista", "activos");

        return "private-pages/vehiculos";
    }

    private String extractPublicId(String url) {
        if (url == null || url.isEmpty()) return null;
        try {
            int uploadIndex = url.indexOf("/upload/");
            if (uploadIndex == -1) return null;

            String pathAfterUpload = url.substring(uploadIndex + "/upload/".length());
            if (pathAfterUpload.matches("^v\\d+/.*")) {
                pathAfterUpload = pathAfterUpload.substring(pathAfterUpload.indexOf("/") + 1);
            }

            int lastDotIndex = pathAfterUpload.lastIndexOf(".");
            return (lastDotIndex != -1) ? pathAfterUpload.substring(0, lastDotIndex) : pathAfterUpload;
        } catch (Exception e) {
            System.err.println("Error al extraer public ID de Cloudinary: " + e.getMessage());
            return null;
        }
    }

    @PostMapping("/editar")
    public String editarVehiculo(@ModelAttribute Vehiculos vehiculo,
                                 @RequestParam(value = "fileFotoEdit", required = false) MultipartFile file,
                                 @RequestParam("fotoActual") String fotoActual,
                                 RedirectAttributes redir) {
        try {
            vehiculo.setObjMarca(repoMarca.findById(vehiculo.getIdMarca()).orElse(null));
            vehiculo.setObjTipo(repoTipo.findById(vehiculo.getIdTipo()).orElse(null));

            if (file != null && !file.isEmpty()) {
                String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
                if (!ext.matches("jpg|jpeg|png")) {
                    redir.addFlashAttribute("mensaje", "La imagen debe estar en formato JPG, JPEG o PNG");
                    redir.addFlashAttribute("css_mensaje", "alert alert-danger");
                    redir.addFlashAttribute("tipoMensaje", "error");
                    return "redirect:/vehiculos/listado";
                }

                if (file.getSize() > 1048576) {
                    redir.addFlashAttribute("mensaje", "La imagen no puede superar 1MB");
                    redir.addFlashAttribute("css_mensaje", "alert alert-danger");
                    redir.addFlashAttribute("tipoMensaje", "error");
                    return "redirect:/vehiculos/listado";
                }

                String publicId = extractPublicId(fotoActual);
                if (publicId != null) cloudinaryService.eliminarImagen(publicId);

                String url = cloudinaryService.subirImagen(file, "vehiculos");
                vehiculo.setFoto(url);
            } else {
                vehiculo.setFoto(fotoActual);
            }

            repoVehiculo.save(vehiculo);
            redir.addFlashAttribute("mensaje", "Vehículo actualizado correctamente");
            redir.addFlashAttribute("css_mensaje", "alert alert-success");
            redir.addFlashAttribute("tipoMensaje", "success");

        } catch (Exception e) {
            redir.addFlashAttribute("mensaje", "Error al actualizar vehículo: " + e.getMessage());
            redir.addFlashAttribute("css_mensaje", "alert alert-danger");
            redir.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/vehiculos/listado";
    }

    @PostMapping("/registro")
    public String registrarVehiculo(@ModelAttribute Vehiculos vehiculos,
                                    @RequestParam("fileFoto") MultipartFile file,
                                    RedirectAttributes redir) {
        try {
            if (file.isEmpty()) {
                redir.addFlashAttribute("mensaje", "Debe seleccionar una imagen para el vehículo.");
                redir.addFlashAttribute("css_mensaje", "alert alert-danger");
                redir.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/vehiculos/listado";
            }

            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            if (!ext.matches("jpg|jpeg|png")) {
                redir.addFlashAttribute("mensaje", "La imagen debe estar en formato JPG, JPEG o PNG");
                redir.addFlashAttribute("css_mensaje", "alert alert-danger");
                redir.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/vehiculos/listado";
            }

            if (file.getSize() > 1048576) {
                redir.addFlashAttribute("mensaje", "La imagen no puede superar 1MB");
                redir.addFlashAttribute("css_mensaje", "alert alert-danger");
                redir.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/vehiculos/listado";
            }

            String secureUrl = cloudinaryService.subirImagen(file, "vehiculos");
            vehiculos.setFoto(secureUrl);
            repoVehiculo.save(vehiculos);

            redir.addFlashAttribute("mensaje", "Vehículo registrado correctamente");
            redir.addFlashAttribute("css_mensaje", "alert alert-success");
            redir.addFlashAttribute("tipoMensaje", "success");

        } catch (Exception e) {
            redir.addFlashAttribute("mensaje", "Error al registrar vehículo: " + e.getMessage());
            redir.addFlashAttribute("css_mensaje", "alert alert-danger");
            redir.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/vehiculos/listado";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable int id, RedirectAttributes redir) {
        try {
            Vehiculos vehiculo = repoVehiculo.findById(id).orElse(null);
            if (vehiculo != null) {
                vehiculo.setEstado(2);
                repoVehiculo.save(vehiculo);
                redir.addFlashAttribute("mensaje", "Vehículo cancelado correctamente");
                redir.addFlashAttribute("css_mensaje", "alert alert-success");
                redir.addFlashAttribute("tipoMensaje", "success");
            }
        } catch (Exception e) {
            redir.addFlashAttribute("mensaje", "Error al eliminar vehículo: " + e.getMessage());
            redir.addFlashAttribute("css_mensaje", "alert alert-danger");
            redir.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/vehiculos/listado";
    }

    @GetMapping("/cancelados")
    public String vehiculosCancelados(Model model) {
        List<Vehiculos> listaCancelados = repoVehiculo.findByEstado(2);
        model.addAttribute("lstVehiculos", listaCancelados);
        model.addAttribute("lstMarcas", repoMarca.findByEstado(1));
        model.addAttribute("lstTipos", repoTipo.findByEstado(1));
        model.addAttribute("vehiculos", new Vehiculos());
        model.addAttribute("vista", "cancelados");
        return "private-pages/vehiculos";
    }

    @GetMapping("/restaurar/{id}")
    public String restaurarVehiculo(@PathVariable int id, RedirectAttributes redir) {
        try {
            Vehiculos vehiculo = repoVehiculo.findById(id).orElse(null);
            if (vehiculo != null) {
                vehiculo.setEstado(1);
                repoVehiculo.save(vehiculo);
                redir.addFlashAttribute("mensaje", "Vehículo restaurado correctamente");
                redir.addFlashAttribute("css_mensaje", "alert alert-success");
                redir.addFlashAttribute("tipoMensaje", "success");
            }
        } catch (Exception e) {
            redir.addFlashAttribute("mensaje", "Error al restaurar vehículo: " + e.getMessage());
            redir.addFlashAttribute("css_mensaje", "alert alert-danger");
            redir.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/vehiculos/cancelados";
    }
    
    @GetMapping("/reporte")
    public void generarReporte(HttpServletResponse response) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=vehiculos.pdf");

        try {
            String ruta = resourceLoader.getResource("classpath:/static/reports/vehiculos.jasper")
                                        .getFile()
                                        .getAbsolutePath();

            HashMap<String, Object> parametros = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, dataSource.getConnection());

            OutputStream outStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
