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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyectosCibertec.com.model.Marcas;
import proyectosCibertec.com.model.Tipos;
import proyectosCibertec.com.model.Vehiculos;
import proyectosCibertec.com.repository.IMarcasRepository;
import proyectosCibertec.com.repository.ITiposRepository;
import proyectosCibertec.com.repository.IVehiculosRepository;
import proyectosCibertec.com.service.CloudinaryService;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {
	@Autowired
	private IVehiculosRepository repoVehiculo;

	@Autowired
	private IMarcasRepository repoMarca;

	@Autowired
	private ITiposRepository repoTipo;

	// Cloudinary
	@Autowired
	private CloudinaryService cloudinaryService;

	@GetMapping("/listado")
	public String vehiculos_crud(Model model) {
		List<Vehiculos> listaVehiculos = repoVehiculo.findAll();
		List<Marcas> listaMarcas = repoMarca.findAll();
		List<Tipos> listaTipos = repoTipo.findAll();

		model.addAttribute("lstVehiculos", listaVehiculos);
		model.addAttribute("lstMarcas", listaMarcas);
		model.addAttribute("lstTipos", listaTipos);

		model.addAttribute("vehiculos", new Vehiculos());

		return "vehiculos";
	}

	// Extraer el public ID de la URL de Cloudinary
	private String extractPublicId(String url) {
		if (url == null || url.isEmpty()) {
			return null;
		}
		try {
			int uploadIndex = url.indexOf("/upload/");
			if (uploadIndex == -1) {
				return null; // No es una URL de Cloudinary o no tiene el formato esperado
			}

			String pathAfterUpload = url.substring(uploadIndex + "/upload/".length());

			if (pathAfterUpload.matches("^v\\d+/.*")) {
				pathAfterUpload = pathAfterUpload.substring(pathAfterUpload.indexOf("/") + 1);
			}

			// Elimina la extensión del archivo.
			int lastDotIndex = pathAfterUpload.lastIndexOf(".");
			if (lastDotIndex != -1) {
				return pathAfterUpload.substring(0, lastDotIndex);
			} else {
				return pathAfterUpload;
			}
		} catch (Exception e) {
			System.err.println("Error al extraer public ID de la URL: " + url + " - " + e.getMessage());
			return null;
		}
	}

	@PostMapping("/editar")
	public String editarVehiculo(@ModelAttribute Vehiculos vehiculo,
			@RequestParam(value = "fileFotoEdit", required = false) MultipartFile file,
			@RequestParam("fotoActual") String fotoActual, RedirectAttributes redirAtributos) {

		try {

			vehiculo.setObjMarca(repoMarca.findById(vehiculo.getIdMarca()).orElse(null));
			vehiculo.setObjTipo(repoTipo.findById(vehiculo.getIdTipo()).orElse(null));

			// Validar si hay nueva foto subida
			if (file != null && !file.isEmpty()) {
				String originalFilename = file.getOriginalFilename();
				String extension = "";
				if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
					extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
				}

				// Validar que sea JPG, JPEG o PNG
				if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
					redirAtributos.addFlashAttribute("mensaje", "La imagen debe estar en formato JPG, JPEG o PNG");
					redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
					return "redirect:/vehiculos/listado";
				}

				// Validación de tamaño máximo 1MB
				if (file.getSize() > 1048576) {
					redirAtributos.addFlashAttribute("mensaje", "La imagen no puede superar 1MB");
					redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
					return "redirect:/vehiculos/listado";
				}

				// Eliminar la foto anterior si se va a reemplazar
				if (fotoActual != null && !fotoActual.isBlank()) {
					String publicIdToDelete = extractPublicId(fotoActual); // Extrae el public ID de la URL actual
					if (publicIdToDelete != null) {
						cloudinaryService.eliminarImagen(publicIdToDelete);
					}
				}

				String secureUrl = cloudinaryService.subirImagen(file, "vehiculos");
				vehiculo.setFoto(secureUrl); // Guardar URL de Cloudinary

			} else {
				// Si no se sube una nueva foto, mantener foto existente
				vehiculo.setFoto(fotoActual);
			}

			repoVehiculo.save(vehiculo);

			redirAtributos.addFlashAttribute("mensaje", "Vehículo actualizado correctamente");
			redirAtributos.addFlashAttribute("css_mensaje", "alert alert-success");

		} catch (Exception e) {
			redirAtributos.addFlashAttribute("mensaje", "Error al actualizar vehículo: " + e.getMessage());
			redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
			e.printStackTrace();
		}
		return "redirect:/vehiculos/listado";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarVehiculo(@PathVariable int id, RedirectAttributes redirAtributos) {
		try {
			Vehiculos vehiculo = repoVehiculo.findById(id).orElse(null);

			// Borrar imagen de Cloudinary
			if (vehiculo != null) {
				String urlFoto = vehiculo.getFoto();
				if (urlFoto != null && !urlFoto.isBlank()) {
					String publicId = extractPublicId(urlFoto);
					if (publicId != null) {
						cloudinaryService.eliminarImagen(publicId);
					}
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
	public String registrarVehiculo(@ModelAttribute Vehiculos vehiculos, @RequestParam("fileFoto") MultipartFile file,
			RedirectAttributes redirAtributos) {
		try {
			// Validación de archivo vacío
			if (file.isEmpty()) {
				redirAtributos.addFlashAttribute("mensaje", "Debe seleccionar una imagen para el vehículo.");
				redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
				return "redirect:/vehiculos/listado";
			}

			// Validar que sea JPG o JPEG
			String originalFilename = file.getOriginalFilename();
			String extension = "";
			if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
				extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
			}
			if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
				redirAtributos.addFlashAttribute("mensaje", "La imagen debe estar en formato JPG, JPEG o PNG");
				redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");

				return "redirect:/vehiculos/listado";
			}

			// Validación de tamaño máximo 1MB
			if (file.getSize() > 1048576) {
				redirAtributos.addFlashAttribute("mensaje", "La imagen no puede superar 1MB");
				redirAtributos.addFlashAttribute("css_mensaje", "alert alert-danger");
				return "redirect:/vehiculos/listado";
			}

			// Subir la imagen a Cloudinary
			String secureUrl = cloudinaryService.subirImagen(file, "vehiculos");
			vehiculos.setFoto(secureUrl);

			// Guardar en la BD
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