package proyectosCibertec.com.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import proyectosCibertec.com.model.Alquiler;
import proyectosCibertec.com.model.Configuracion;
import proyectosCibertec.com.model.Vehiculos;
import proyectosCibertec.com.repository.IAlquilerRepository;
import proyectosCibertec.com.repository.IClienteRepository;
import proyectosCibertec.com.repository.IConfiguracionRepository;
import proyectosCibertec.com.repository.IDocumentosRepository;
import proyectosCibertec.com.repository.IMarcasRepository;
import proyectosCibertec.com.repository.IVehiculosRepository;
import proyectosCibertec.com.security.UsuarioDetailsSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import proyectosCibertec.com.model.Usuario;

@Controller
@RequestMapping("/alquiler")
public class AlquilerController {

	@Autowired
	private IAlquilerRepository alquilerRepository;

	@Autowired
	private IDocumentosRepository documentosRepository;

	@Autowired
	private IClienteRepository clientesRepository;

	@Autowired
	private IVehiculosRepository vehiculosRepository;

	@Autowired
	private IConfiguracionRepository configuracionRepository;

	@Autowired
	private IMarcasRepository marcasRepository;

	@GetMapping("/listado")
	public String alquilerVista(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {	
		
		Page<Alquiler> pagina = alquilerRepository.findAll(PageRequest.of(page, size));
		model.addAttribute("pagina", pagina);
		model.addAttribute("paginaActual", page);
		model.addAttribute("tamanio", size);
		model.addAttribute("alquiler", new Alquiler());

		return "alquiler/listadoAlquiler";
	}

	@GetMapping("/crear")
	public String mostrarFormularioRegistro(Model model) {
		model.addAttribute("alquiler", new Alquiler());
		model.addAttribute("lstClientes", clientesRepository.findAll());
		model.addAttribute("lstVehiculos", vehiculosRepository.findByActividad("LIBRE"));
		model.addAttribute("lstMarcas", marcasRepository.findAll());
		model.addAttribute("lstDocumentos", documentosRepository.findAll());


		Configuracion configuracion = configuracionRepository.findById(1).orElse(new Configuracion());
		model.addAttribute("penalidadXdia", configuracion.getPenalidadPorDia());
		model.addAttribute("moneda", configuracion.getMoneda());

		return "alquiler/crearAlquiler";
	}

	@PostMapping("/registrar")
	public String registrarAlquiler(@ModelAttribute("alquiler") Alquiler alquiler) {

		try {
			Configuracion configuracion = configuracionRepository.findById(1).orElse(new Configuracion());
			Vehiculos vehiculo=vehiculosRepository.findById(alquiler.getIdVehiculo()).orElse(null);
			
			alquiler.setPenalidadPorDia(configuracion.getPenalidadPorDia());
			alquiler.setIdMoneda(configuracion.getMoneda().getId());
			alquiler.setPrecioDia(BigDecimal.valueOf((vehiculo.getPrecioXDia())));
			
			alquilerRepository.save(alquiler);
			vehiculo.setActividad("PRESTADO");
			
			vehiculosRepository.save(vehiculo);
			
		} catch (Exception e) {
			
			throw new RuntimeException("Error al registrar el alquiler", e);
		}

		return "redirect:/alquiler/listado";
	}

	@GetMapping("/editar/{id}")
	public String editarVista(@PathVariable int id, Model model) {
		Alquiler alquiler = alquilerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("ID inválido"));
		model.addAttribute("alquiler", alquiler);
		model.addAttribute("lstClientes", clientesRepository.findAll());
		model.addAttribute("lstVehiculos", vehiculosRepository.findAll());
		model.addAttribute("lstDocumentos", documentosRepository.findAll());
		model.addAttribute("lstMarcas", marcasRepository.findAll());

		Configuracion configuracion = configuracionRepository.findById(1).get();
		model.addAttribute("penalidadXdia", configuracion.getPenalidadPorDia());
		model.addAttribute("moneda", configuracion.getMoneda());

		return "alquiler/editarAlquiler"; 
	}


	@PostMapping("/editar")
	public String editarGuardar(@ModelAttribute("alquiler") Alquiler alquilerForm) {
		Alquiler alquilerBD = alquilerRepository.findById(alquilerForm.getId())
				.orElseThrow(() -> new IllegalArgumentException("ID inválido"));

		alquilerBD.setAbono(alquilerForm.getAbono());
		if(alquilerForm.getEstado()!=null)
		alquilerBD.setEstado(alquilerForm.getEstado());
		alquilerRepository.save(alquilerBD);

		if (alquilerForm.getEstado()!=null && !alquilerForm.getEstado().contentEquals("EN PRESTAMO")) {
			Vehiculos vehiculo = alquilerBD.getVehiculo();
			vehiculo.setActividad("LIBRE");
			vehiculosRepository.save(vehiculo);
			
			if (alquilerForm.getEstado().equalsIgnoreCase("DEVUELTO")) {
	           
	            LocalDate fechaHoy = LocalDate.now();
	            alquilerBD.setFechaRealDevolucion(fechaHoy);

	            LocalDate fechaEstimada = alquilerBD.getFechaEstimadaDevolucion();
	            long diasRetrasados = java.time.temporal.ChronoUnit.DAYS.between(fechaEstimada, fechaHoy);

	            if (diasRetrasados > 0) {
	                alquilerBD.setNumDias((int) diasRetrasados);
	                
	                // Calcular penalidad = días retraso * (precio día + penalidad día)
	                BigDecimal precioDia = alquilerBD.getPrecioDia();
	                BigDecimal penalidadDia = BigDecimal.valueOf(alquilerBD.getPenalidadPorDia());
	                BigDecimal totalPenalidad = (precioDia.add(penalidadDia)).multiply(BigDecimal.valueOf(diasRetrasados));
	                
	                alquilerBD.setPenalidad(totalPenalidad);
	            } else {
	                alquilerBD.setNumDias(0);
	                alquilerBD.setPenalidad(BigDecimal.ZERO);
	            }

	            alquilerRepository.save(alquilerBD);
	        }
		}

		return "redirect:/alquiler/listado";
	}

	@GetMapping("/vehiculosPorMarca")
	@ResponseBody
	public List<Vehiculos> obtenerVehiculosPorMarca(@RequestParam("idMarca") int idMarca) {
		return vehiculosRepository.findByIdMarcaAndEstadoAndActividad(idMarca, 1,"LIBRE");
	}
	
	@GetMapping("/detalle/{id}")
	public String detalleAlquiler(@PathVariable int id, Model model) {
	    Alquiler alquiler = alquilerRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("ID de alquiler no válido: " + id));

	    model.addAttribute("alquiler", alquiler);
	    return "alquiler/detalleAlquiler";
	}

}
