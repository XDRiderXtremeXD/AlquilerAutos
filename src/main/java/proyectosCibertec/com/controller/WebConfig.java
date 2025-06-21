package proyectosCibertec.com.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Para exponer archivos desde la carpeta externa "uploads/img/vehiculos/" (Ya que esta fuera de src)
    	
        // Vehículos
        registry.addResourceHandler("/img/vehiculos/**")
                .addResourceLocations("file:uploads/img/vehiculos/");

        // Documentos
        registry.addResourceHandler("/img/documentos/**")
                .addResourceLocations("file:uploads/img/documentos/");
        
        // Agrega el tuyo en caso necesite registrar imagen
    }
}