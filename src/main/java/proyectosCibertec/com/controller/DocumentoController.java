package proyectosCibertec.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyectosCibertec.com.model.Documentos;
import proyectosCibertec.com.repository.IDocumentosRepository;
import proyectosCibertec.com.service.CloudinaryService;

@Controller
@RequestMapping("/documentos")
public class DocumentoController {

    @Autowired
    private IDocumentosRepository repoDocumentos;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/listado")
    public String documentos_crud(Model model) {
        List<Documentos> lista = repoDocumentos.findByEstado(1);
        model.addAttribute("lstDocumentos", lista);
        model.addAttribute("documentos", new Documentos());
        model.addAttribute("vista", "activos");
        return "documentos";
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
            System.err.println("Error al extraer public ID: " + e.getMessage());
            return null;
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDocumento(@PathVariable int id, RedirectAttributes redirAtributos) {
        try {
            Documentos documento = repoDocumentos.findById(id).orElse(null);
            if (documento != null) {
                documento.setEstado(2);
                repoDocumentos.save(documento);
                redirAtributos.addFlashAttribute("mensaje", "Documento cancelado correctamente");
                redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
                redirAtributos.addFlashAttribute("tipoMensaje", "success");
            }
        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al cancelar documento: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/documentos/listado";
    }

    @PostMapping("/editar")
    public String editarDocumento(@ModelAttribute Documentos documentos,
            @RequestParam(value = "fileFotoEdit", required = false) MultipartFile file,
            @RequestParam("fotoActual") String fotoActual, RedirectAttributes redirAtributos) {
        try {
            if (file != null && !file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
                }

                if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
                    redirAtributos.addFlashAttribute("mensaje", "La imagen debe estar en formato JPG, JPEG o PNG");
                    redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
                    redirAtributos.addFlashAttribute("tipoMensaje", "error");
                    return "redirect:/documentos/listado";
                }

                if (file.getSize() > 1048576) {
                    redirAtributos.addFlashAttribute("mensaje", "La imagen no puede superar 1MB");
                    redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
                    redirAtributos.addFlashAttribute("tipoMensaje", "error");
                    return "redirect:/documentos/listado";
                }

                if (fotoActual != null && !fotoActual.isBlank()) {
                    String publicIdToDelete = extractPublicId(fotoActual);
                    if (publicIdToDelete != null) {
                        cloudinaryService.eliminarImagen(publicIdToDelete);
                    }
                }

                String secureUrl = cloudinaryService.subirImagen(file, "documentos");
                documentos.setFoto(secureUrl);
            } else {
                documentos.setFoto(fotoActual);
            }

            repoDocumentos.save(documentos);
            redirAtributos.addFlashAttribute("mensaje", "Documento actualizado correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
            redirAtributos.addFlashAttribute("tipoMensaje", "success");

        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al actualizar documento: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/documentos/listado";
    }

    @PostMapping("/registro")
    public String registrarDocumento(@ModelAttribute Documentos documentos,
            @RequestParam("fileFoto") MultipartFile file, RedirectAttributes redirAtributos) {
        try {
            if (file.isEmpty()) {
                redirAtributos.addFlashAttribute("mensaje", "Debe seleccionar una imagen para el documento.");
                redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
                redirAtributos.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/documentos/listado";
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
                extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            }

            if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
                redirAtributos.addFlashAttribute("mensaje", "La imagen debe estar en formato JPG, JPEG o PNG");
                redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
                redirAtributos.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/documentos/listado";
            }

            if (file.getSize() > 1048576) {
                redirAtributos.addFlashAttribute("mensaje", "La imagen no puede superar 1MB");
                redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
                redirAtributos.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/documentos/listado";
            }

            String secureUrl = cloudinaryService.subirImagen(file, "documentos");
            documentos.setFoto(secureUrl);
            repoDocumentos.save(documentos);

            redirAtributos.addFlashAttribute("mensaje", "Documento registrado correctamente");
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
            redirAtributos.addFlashAttribute("tipoMensaje", "success");

        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al registrar documento: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/documentos/listado";
    }

    @GetMapping("/cancelados")
    public String documentosCancelados(Model model) {
        List<Documentos> lista = repoDocumentos.findByEstado(2);
        model.addAttribute("lstDocumentos", lista);
        model.addAttribute("documentos", new Documentos());
        model.addAttribute("vista", "cancelados");
        return "documentos";
    }

    @GetMapping("/restaurar/{id}")
    public String restaurarDocumento(@PathVariable int id, RedirectAttributes redirAtributos) {
        try {
            Documentos documento = repoDocumentos.findById(id).orElse(null);
            if (documento != null) {
                documento.setEstado(1);
                repoDocumentos.save(documento);
                redirAtributos.addFlashAttribute("mensaje", "Documento restaurado correctamente");
                redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");
                redirAtributos.addFlashAttribute("tipoMensaje", "success");
            }
        } catch (Exception e) {
            redirAtributos.addFlashAttribute("mensaje", "Error al restaurar documento: " + e.getMessage());
            redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
            redirAtributos.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/documentos/cancelados";
    }
}
