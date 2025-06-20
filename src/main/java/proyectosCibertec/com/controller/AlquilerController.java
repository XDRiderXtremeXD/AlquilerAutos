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
import proyectosCibertec.com.repository.IAlquilerRepository;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;


@Controller
@RequestMapping("/alquiler")
public class AlquilerController {

    private final DocumentoController documentoController;

    @Autowired
    private IAlquilerRepository alquilerRepository;

    public AlquilerController(DocumentoController documentoController) {
        this.documentoController = documentoController;
    }

    @GetMapping("/listado")
    public String alquilerVista(Model model, @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size) {

        Page<Alquiler> pagina = alquilerRepository.findAll(PageRequest.of(page, size));
        model.addAttribute("pagina", pagina);
        model.addAttribute("paginaActual", page);
        model.addAttribute("tamanio", size);

        return "alquiler";
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
                row.createCell(13).setCellValue(a.getPenalidad_por_dia() != null ? a.getPenalidad_por_dia().doubleValue() : 0);
                row.createCell(14).setCellValue(a.getEstado() == 1 ? "Activo" : "Inactivo");
            }

            workbook.write(response.getOutputStream());
        }
    }

}
