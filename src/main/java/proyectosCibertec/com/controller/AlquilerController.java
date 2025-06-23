package proyectosCibertec.com.controller;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import jakarta.servlet.http.HttpServletResponse;

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

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;


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
        model.addAttribute("lstVehiculos", vehiculosRepository.findAll());
        model.addAttribute("lstMarcas", marcasRepository.findAll());
        model.addAttribute("lstDocumentos", documentosRepository.findAll());

        Configuracion configuracion = configuracionRepository.findById(1).orElse(new Configuracion());
        model.addAttribute("penalidadXdia", configuracion.getPenalidadPorDia());
        model.addAttribute("moneda", configuracion.getMoneda());

        return "alquiler/crearAlquiler"; 
    }
    
    @PostMapping("/registrar")
    public String registrarAlquiler(@ModelAttribute("alquiler") Alquiler alquiler) {
        alquiler.setEstado(1); 
        Configuracion configuracion = configuracionRepository.findById(1).orElse(new Configuracion());
        alquiler.setPenalidadPorDia(configuracion.getPenalidadPorDia());
        alquiler.setIdMoneda(configuracion.getMoneda().getId());
        
        alquilerRepository.save(alquiler);
        return "redirect:/alquiler/listado";
    }


    
    @GetMapping("/editar/{id}")
    public String editarVista(@PathVariable int id, Model model) {
        Alquiler alquiler = alquilerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido"));
        model.addAttribute("alquiler", alquiler);
        model.addAttribute("lstClientes", clientesRepository.findAll());
        model.addAttribute("lstVehiculos", vehiculosRepository.findAll());
        model.addAttribute("lstDocumentos", documentosRepository.findAll());
        model.addAttribute("lstMarcas", marcasRepository.findAll());
        
        Configuracion configuracion = configuracionRepository.findById(1).get();
        model.addAttribute("penalidadXdia",configuracion.getPenalidadPorDia());
        model.addAttribute("moneda",configuracion.getMoneda());
        
        return "alquiler/editarAlquiler"; // Tu nueva vista para editar
    }
    
    @GetMapping("/vehiculosPorMarca")
    @ResponseBody
    public List<Vehiculos> obtenerVehiculosPorMarca(@RequestParam("idMarca") int idMarca) {
        return vehiculosRepository.findByIdMarcaAndEstado(idMarca, 1);
    }
    
    @GetMapping("/exportarPDF")
    public void exportarPDF(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=alquileres.pdf");

        List<Alquiler> lista = alquilerRepository.findAll();

        Document documento = new Document(PageSize.A4.rotate()); // Horizontal
        PdfWriter.getInstance(documento, response.getOutputStream());
        documento.open();

        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph titulo = new Paragraph("Listado de Alquileres", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        documento.add(Chunk.NEWLINE);

        PdfPTable tabla = new PdfPTable(13);
        tabla.setWidthPercentage(100);

        String[] columnas = {
            "ID", "Cliente", "Vehículo", "Días", "Precio/Día", "Abono", "Fecha Préstamo",
            "Hora", "F. Estimada", "F. Real", "Doc", "Penalidad", "Estado"
        };

        for (String cabecera : columnas) {
            PdfPCell celda = new PdfPCell(new Phrase(cabecera));
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tabla.addCell(celda);
        }

        for (Alquiler a : lista) {
            tabla.addCell(String.valueOf(a.getId()));
            tabla.addCell(String.valueOf(a.getIdCliente()));
            tabla.addCell(String.valueOf(a.getIdVehiculo()));
            tabla.addCell(String.valueOf(a.getNumDias()));
            tabla.addCell(a.getPrecioDia() != null ? a.getPrecioDia().setScale(2, RoundingMode.HALF_UP).toString() : "-");
            tabla.addCell(a.getAbono() != null ? a.getAbono().setScale(2, RoundingMode.HALF_UP).toString() : "-");
            tabla.addCell(a.getFechaPrestamo() != null ? a.getFechaPrestamo().toString() : "-");
            tabla.addCell(a.getHora() != null ? a.getHora().toString() : "-");
            tabla.addCell(a.getFechaEstimadaDevolucion() != null ? a.getFechaEstimadaDevolucion().toString() : "-");
            tabla.addCell(a.getFechaRealDevolucion() != null ? a.getFechaRealDevolucion().toString() : "-");
            tabla.addCell(String.valueOf(a.getIdDoc()));
            tabla.addCell(a.getPenalidad() != null ? a.getPenalidad().setScale(2, RoundingMode.HALF_UP).toString() : "-");
            tabla.addCell(a.getEstado() == 1 ? "Activo" : "Inactivo");
        }

        documento.add(tabla);
        documento.close();
    }
    
    @GetMapping("/exportarExcel")
    public void exportarExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=alquileres.xlsx");

        List<Alquiler> lista = alquilerRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Alquileres");
            Row header = sheet.createRow(0);

            String[] columnas = {
                "ID", "Cliente", "Vehículo", "Días", "Precio/Día", "Abono", "Fecha Préstamo", "Hora",
                "Fecha Estimada", "Fecha Real", "Doc", "Observación", "Penalidad", "Penalidad por Día", "Estado"
            };

            // Crear cabeceras
            for (int i = 0; i < columnas.length; i++) {
                header.createCell(i).setCellValue(columnas[i]);
            }

            // Insertar datos
            int fila = 1;
            for (Alquiler a : lista) {
                Row row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(a.getId());
                row.createCell(1).setCellValue(a.getIdCliente());
                row.createCell(2).setCellValue(a.getIdVehiculo());
                row.createCell(3).setCellValue(a.getNumDias());
                row.createCell(4).setCellValue(a.getPrecioDia().doubleValue());
                row.createCell(5).setCellValue(a.getAbono().doubleValue());
                row.createCell(6).setCellValue(a.getFechaPrestamo().toString());
                row.createCell(7).setCellValue(a.getHora().toString());
                row.createCell(8).setCellValue(a.getFechaEstimadaDevolucion() != null ? a.getFechaEstimadaDevolucion().toString() : "-");
                row.createCell(9).setCellValue(a.getFechaRealDevolucion() != null ? a.getFechaRealDevolucion().toString() : "-");
                row.createCell(10).setCellValue(a.getIdDoc());
                row.createCell(11).setCellValue(a.getObservacion() != null ? a.getObservacion() : "");
                row.createCell(12).setCellValue(a.getPenalidad() != null ? a.getPenalidad().doubleValue() : 0);
                row.createCell(13).setCellValue(a.getPenalidadPorDia() != 0 ? a.getPenalidadPorDia() : 0);
                row.createCell(14).setCellValue(a.getEstado() == 1 ? "Activo" : "Inactivo");
            }

            workbook.write(response.getOutputStream());
        }
    }

}
