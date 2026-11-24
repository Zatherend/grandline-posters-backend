package cl.duoc.grandline.backend.dto;

import lombok.Data;

@Data
public class RegistroRequest {
    private String nombre;
    private String apellidos;
    private String correo;
    private String password;
    private String calle;
    private String depto;
    private String comuna;
}