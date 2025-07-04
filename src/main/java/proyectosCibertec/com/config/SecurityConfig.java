package proyectosCibertec.com.config;

import proyectosCibertec.com.service.UsuarioDetailsServiceImpl;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UsuarioDetailsServiceImpl userDetailsService) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers(
            			    "/login",
            			    "/css/**",
            			    "/js/**",
            			    "/images/**",
            			    "/",
            			    "/home",
            			    "/catalogo",
            			    "/quienessomos",
            			    "/contactos"
            			).permitAll()
            			    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/alquiler/listado", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
           .exceptionHandling(e -> e
                .accessDeniedPage("/error")
            );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(UsuarioDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}