package proyectosCibertec.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import proyectosCibertec.com.model.Usuario;
import proyectosCibertec.com.repository.IUsuarioRepository;

@Controller
public class LoginController {

    @Autowired
    private IUsuarioRepository repoUsu;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String nomUsuario,
                                 @RequestParam String clave,
                                 Model model,
                                 HttpSession session) {
    	
        Usuario usuario = repoUsu.findByNomUsuarioAndClave(nomUsuario, clave);
           
        if (usuario != null && usuario.getEstado() == 1) {
            session.setAttribute("usuarioLogueado", usuario); // guarda el usuario en la sesión
            return "redirect:/index"; // puedes redirigir a cualquier vista que desees
        } else {
            model.addAttribute("error", "Usuario o clave incorrectos ");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate(); // destruye la sesión
        return "redirect:/login";
    }
}