package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import proyectosCibertec.com.model.Configuracion;
import proyectosCibertec.com.repository.IConfiguracionRepository;

import proyectosCibertec.com.repository.IMarcasRepository;
import proyectosCibertec.com.repository.IVehiculosRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyectosCibertec.com.model.Vehiculos;
import proyectosCibertec.com.model.Marcas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClientesPageController {

	@Autowired
	private IConfiguracionRepository repoConfig;

	@Autowired
	private IVehiculosRepository repoVehiculo;

	@Autowired
	private IMarcasRepository repoMarca;

	@GetMapping("/contactos")
	public String contactosVista(Model model) {
		Configuracion configuracion = repoConfig.findById(1).orElseThrow();
		model.addAttribute("configuracion", configuracion);
		return "contentclient/contactos";
	}

	@GetMapping("/error/403")
	public String error403() {
		return "contentclient/403";
	}

	@GetMapping("/catalogo")
	public String catalogoVista(@RequestParam(name = "nombre", required = false) String nombre,
			@RequestParam(name = "precioMin", required = false) Double precioMin,
			@RequestParam(name = "precioMax", required = false) Double precioMax,
			@RequestParam(name = "marcaId", required = false) Integer marcaId,
			@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageable = PageRequest.of(page, 10, Sort.by("modelo").ascending());

		// Filtro dinámico
		Specification<Vehiculos> spec = Specification.where((root, query, cb) -> cb.equal(root.get("estado"), 1));

		if (nombre != null && !nombre.isEmpty()) {
			spec = spec
					.and((root, query, cb) -> cb.like(cb.lower(root.get("modelo")), "%" + nombre.toLowerCase() + "%"));
		}

		if (precioMin != null) {
			spec = spec.and((root, query, cb) -> cb.ge(root.get("precioXDia"), precioMin));
		}

		if (precioMax != null) {
			spec = spec.and((root, query, cb) -> cb.le(root.get("precioXDia"), precioMax));
		}

		if (marcaId != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("idMarca"), marcaId));
		}

		Page<Vehiculos> pagina = repoVehiculo.findAll(spec, pageable);
		List<Marcas> marcas = repoMarca.findByEstado(1);

		Map<String, Object> filtros = new HashMap<>();
		filtros.put("nombre", nombre);
		filtros.put("precioMin", precioMin);
		filtros.put("precioMax", precioMax);
		filtros.put("marcaId", marcaId);

		model.addAttribute("vehiculos", pagina);
		model.addAttribute("marcas", marcas);
		model.addAttribute("param", filtros);

		return "contentclient/catalogo";
	}

	@GetMapping("/quienessomos")
	public String quienessomosVista() {
		return "contentclient/quienes-somos";
	}

	@GetMapping("/home")
	public String homeVista(Model model) {
		List<Vehiculos> vehiculosDestacados = repoVehiculo.findTop4ByEstadoOrderByIdDesc(1);
		model.addAttribute("vehiculosDestacados", vehiculosDestacados);
		return "contentclient/home";
	}

	@GetMapping("/")
	public String redirectToHome() {
		return "redirect:/home";
	}
}
