package cl.duoc.grandline.backend.controller;

import cl.duoc.grandline.backend.dto.ProductoRequest;
import cl.duoc.grandline.backend.model.Producto;
import cl.duoc.grandline.backend.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Productos (v1 y v2)", description = "Gestión CRUD de carteles Wanted con versionado de API")
public class ProductoController {

    private final ProductoService productoService;

    // --- VERSION 1 ---
    @GetMapping("/v1/productos")
    @Operation(summary = "[v1] Listar todos los productos")
    public ResponseEntity<List<Producto>> listarV1() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/v1/productos/{id}")
    @Operation(summary = "[v1] Obtener detalle de un producto por ID")
    public ResponseEntity<?> obtenerV1(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productoService.obtenerPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PostMapping("/v1/productos")
    @Operation(summary = "[v1] Crear un nuevo producto (Solo ADMIN)")
    public ResponseEntity<Producto> crearV1(@RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(request));
    }

    // --- VERSION 2 ---
    @GetMapping("/v2/productos")
    @Operation(summary = "[v2] Listar catálogo completo con detalles de categoría y recompensa")
    public ResponseEntity<List<Producto>> listarV2() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/v2/productos/criticos")
    @Operation(summary = "[v2] Listar productos con stock crítico (<= 5 unidades)")
    public ResponseEntity<List<Producto>> listarCriticosV2() {
        return ResponseEntity.ok(productoService.listarCriticos());
    }

    @PostMapping("/v2/productos")
    @Operation(summary = "[v2] Crear producto completo (Solo ADMIN)")
    public ResponseEntity<Producto> crearV2(@RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(request));
    }

    @PutMapping("/v2/productos/{id}")
    @Operation(summary = "[v2] Actualizar producto existente (Solo ADMIN)")
    public ResponseEntity<?> actualizarV2(@PathVariable Long id, @RequestBody ProductoRequest request) {
        try {
            return ResponseEntity.ok(productoService.actualizar(id, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", e.getMessage()));
        }
    }

    @DeleteMapping("/v2/productos/{id}")
    @Operation(summary = "[v2] Eliminar producto del inventario (Solo ADMIN)")
    public ResponseEntity<?> eliminarV2(@PathVariable Long id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", e.getMessage()));
        }
    }
}