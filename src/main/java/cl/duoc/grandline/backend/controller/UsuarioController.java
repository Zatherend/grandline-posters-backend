package cl.duoc.grandline.backend.controller;

import cl.duoc.grandline.backend.dto.PerfilUpdateRequest;
import cl.duoc.grandline.backend.model.Usuario;
import cl.duoc.grandline.backend.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios y Roles (v1)", description = "Administración de usuarios y actualización de perfil")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios registrados (Solo ADMIN)")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PutMapping("/{id}/rol")
    @Operation(summary = "Cambiar rol de un usuario: ADMIN, VENDEDOR o CLIENTE (Solo ADMIN)")
    public ResponseEntity<?> cambiarRol(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoRol = body.get("rol");
        if (nuevoRol == null || (!nuevoRol.equalsIgnoreCase("ADMIN")
                && !nuevoRol.equalsIgnoreCase("VENDEDOR")
                && !nuevoRol.equalsIgnoreCase("CLIENTE"))) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Rol inválido. Use ADMIN, VENDEDOR o CLIENTE."));
        }

        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setRol(nuevoRol.toUpperCase());
                    return ResponseEntity.ok(usuarioRepository.save(usuario));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/perfil")
    @Operation(summary = "Actualizar datos personales del usuario autenticado")
    public ResponseEntity<?> actualizarPerfil(@RequestBody PerfilUpdateRequest datos, Authentication auth) {
        return usuarioRepository.findByCorreo(auth.getName().toLowerCase())
                .map(usuario -> {
                    if (datos.getNombre() != null) usuario.setNombre(datos.getNombre());
                    if (datos.getApellidos() != null) usuario.setApellidos(datos.getApellidos());
                    if (datos.getCalle() != null) usuario.setCalle(datos.getCalle());
                    if (datos.getDepto() != null) usuario.setDepto(datos.getDepto());
                    if (datos.getComuna() != null) usuario.setComuna(datos.getComuna());
                    return ResponseEntity.ok(usuarioRepository.save(usuario));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario (Solo ADMIN)")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}