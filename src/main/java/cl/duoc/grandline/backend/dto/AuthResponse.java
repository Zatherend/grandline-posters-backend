package cl.duoc.grandline.backend.dto;

import cl.duoc.grandline.backend.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Usuario usuario;
}

