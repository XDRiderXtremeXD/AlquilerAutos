package proyectosCibertec.com.security;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import proyectosCibertec.com.model.Usuario;

import java.util.Collection;
import java.util.Collections;

public class UsuarioDetailsSession implements UserDetails {


	private static final long serialVersionUID = 1L;
	private Usuario usuario = new Usuario();

    public UsuarioDetailsSession(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_"+(this.usuario.getRol()));
    }

    @Override
    public String getPassword() {
        return usuario.getClave();
    }

    @Override
    public String getUsername() {
        return usuario.getNomUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario.getEstado() == 1;
    }
}
