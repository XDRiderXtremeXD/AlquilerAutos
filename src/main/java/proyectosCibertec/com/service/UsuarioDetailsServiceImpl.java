package proyectosCibertec.com.service;

import org.springframework.stereotype.Service;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import proyectosCibertec.com.model.Usuario;
import proyectosCibertec.com.repository.IUsuarioRepository;


@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNomUsuario(username);

        if (usuario == null || usuario.getEstado() != 1) {
            throw new UsernameNotFoundException("Usuario no encontrado o inactivo");
        }

        return new org.springframework.security.core.userdetails.User(
            usuario.getNomUsuario(),
            usuario.getClave(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}