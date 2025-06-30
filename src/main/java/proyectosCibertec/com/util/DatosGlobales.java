package proyectosCibertec.com.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import proyectosCibertec.com.security.UsuarioDetailsSession;

@ControllerAdvice
public class DatosGlobales {
	
	 @ModelAttribute
	    public void logRequest(HttpServletRequest request) {
	        System.out.println("🔍 Accediendo a: " + request.getRequestURI());
	    }

    @ModelAttribute
    public void agregarUsuarioAlModelo(Model model) {
    	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();

         if (auth != null && auth.getPrincipal() instanceof UsuarioDetailsSession) {
             UsuarioDetailsSession userDetails = (UsuarioDetailsSession) auth.getPrincipal();

             String foto = userDetails.getUsuario().getPerfil();
             String nombre = userDetails.getUsuario().getNomUsuario();

             String rol = userDetails.getAuthorities().stream()
                     .map(GrantedAuthority::getAuthority)
                     .findFirst()
                     .orElse("SIN_ROL");

             model.addAttribute("fotoUsuario", foto);
             model.addAttribute("nombreUsuario", nombre);
             model.addAttribute("rolUsuario", rol);
         }
    }
}
