package cl.duoc.grandline.backend.dto;

import lombok.Data;

@Data
public class ProductoRequest {
    private String nombre;
    private Long recompensa;
    private Integer precio;
    private Integer stock;
    private String categoria;
    private String imagen;
    private String descripcion;
}