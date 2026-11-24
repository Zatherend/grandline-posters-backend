package cl.duoc.grandline.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class CompraRequest {
    private String calleEnvio;
    private String deptoEnvio;
    private String comunaEnvio;
    private List<ItemCompraDTO> items;

    @Data
    public static class ItemCompraDTO {
        private Long productoId;
        private Integer cantidad;
    }
}