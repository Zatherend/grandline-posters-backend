package cl.duoc.grandline.backend.security;

import cl.duoc.grandline.backend.model.Usuario;
import cl.duoc.grandline.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        return new User(
                usuario.getCorreo(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toUpperCase()))
        );
    }
}