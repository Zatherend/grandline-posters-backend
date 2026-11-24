package cl.duoc.grandline.backend.service;

import cl.duoc.grandline.backend.dto.AuthResponse;
import cl.duoc.grandline.backend.dto.LoginRequest;
import cl.duoc.grandline.backend.dto.RegistroRequest;
import cl.duoc.grandline.backend.model.Usuario;
import cl.duoc.grandline.backend.repository.UsuarioRepository;
import cl.duoc.grandline.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final Pattern CORREO_DUOC_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@(duocuc\\.cl|duoc\\.cl)$", Pattern.CASE_INSENSITIVE);

    public boolean validarCorreoDuoc(String correo) {
        return correo != null && CORREO_DUOC_PATTERN.matcher(correo.trim()).matches();
    }

    public AuthResponse registrar(RegistroRequest request) {
        String correoNorm = request.getCorreo() != null ? request.getCorreo().trim().toLowerCase() : "";

        if (!validarCorreoDuoc(correoNorm)) {
            throw new IllegalArgumentException("El correo debe ser exclusivamente @duocuc.cl o @duoc.cl");
        }
        if (usuarioRepository.existsByCorreo(correoNorm)) {
            throw new IllegalArgumentException("Ya existe una cuenta registrada con este correo institucional.");
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(request.getNombre());
        nuevo.setApellidos(request.getApellidos());
        nuevo.setCorreo(correoNorm);
        nuevo.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevo.setRol("CLIENTE");
        nuevo.setCalle(request.getCalle() != null ? request.getCalle() : "");
        nuevo.setDepto(request.getDepto() != null ? request.getDepto() : "");
        nuevo.setComuna(request.getComuna() != null ? request.getComuna() : "Santiago");

        Usuario guardado = usuarioRepository.save(nuevo);
        String token = jwtUtil.generarToken(guardado.getCorreo(), guardado.getRol());
        return new AuthResponse(token, guardado);
    }

    public AuthResponse login(LoginRequest request) {
        String correoNorm = request.getCorreo() != null ? request.getCorreo().trim().toLowerCase() : "";

        if (!validarCorreoDuoc(correoNorm)) {
            throw new IllegalArgumentException("Solo se permite el acceso con correos @duocuc.cl o @duoc.cl");
        }

        Usuario usuario = usuarioRepository.findByCorreo(correoNorm)
                .orElseThrow(() -> new IllegalArgumentException("Correo o contraseña incorrectos."));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new IllegalArgumentException("Correo o contraseña incorrectos.");
        }

        String token = jwtUtil.generarToken(usuario.getCorreo(), usuario.getRol());
        return new AuthResponse(token, usuario);
    }
}