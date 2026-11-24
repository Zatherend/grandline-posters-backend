package cl.duoc.grandline.backend.dto;

import lombok.Data;

@Data
public class PerfilUpdateRequest {
    private String nombre;
    private String apellidos;
    private String calle;
    private String depto;
    private String comuna;
}