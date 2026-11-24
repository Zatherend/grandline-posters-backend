package cl.duoc.grandline.backend.controller;

import cl.duoc.grandline.backend.dto.CompraRequest;
import cl.duoc.grandline.backend.model.Boleta;
import cl.duoc.grandline.backend.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/boletas")
@RequiredArgsConstructor
@Tag(name = "Boletas y Órdenes (v1)", description = "Gestión de compras, historial de clientes y supervisión de ventas")
public class BoletaController {

    private final BoletaService boletaService;

    @PostMapping
    @Operation(summary = "Registrar una nueva compra y descontar stock (Usuario autenticado)")
    public ResponseEntity<?> crearCompra(@RequestBody CompraRequest request, Authentication auth) {
        try {
            Boleta boleta = boletaService.generarCompra(auth.getName(), request);
            return ResponseEntity.status(HttpStatus.CREATED).body(boleta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping("/mis-compras")
    @Operation(summary = "Obtener historial de boletas del usuario autenticado")
    public ResponseEntity<List<Boleta>> misCompras(Authentication auth) {
        return ResponseEntity.ok(boletaService.listarPorUsuario(auth.getName()));
    }

    @GetMapping
    @Operation(summary = "Listar todas las órdenes del sistema (ADMIN y VENDEDOR)")
    public ResponseEntity<List<Boleta>> listarTodas() {
        return ResponseEntity.ok(boletaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de una boleta específica (ADMIN y VENDEDOR)")
    public ResponseEntity<?> obtenerDetalle(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(boletaService.obtenerPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", e.getMessage()));
        }
    }
}