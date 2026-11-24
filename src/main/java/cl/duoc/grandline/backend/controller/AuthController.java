package cl.duoc.grandline.backend.controller;

import cl.duoc.grandline.backend.dto.AuthResponse;
import cl.duoc.grandline.backend.dto.LoginRequest;
import cl.duoc.grandline.backend.dto.RegistroRequest;
import cl.duoc.grandline.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación (v1)", description = "Endpoints públicos para login y registro con emisión de JWT")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener token JWT")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar nuevo cliente institucional (@duocuc.cl / @duoc.cl)")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest request) {
        try {
            AuthResponse response = authService.registrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }
}