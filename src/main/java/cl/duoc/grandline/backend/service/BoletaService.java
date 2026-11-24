package cl.duoc.grandline.backend.service;

import cl.duoc.grandline.backend.dto.CompraRequest;
import cl.duoc.grandline.backend.model.Boleta;
import cl.duoc.grandline.backend.model.DetalleBoleta;
import cl.duoc.grandline.backend.model.Producto;
import cl.duoc.grandline.backend.model.Usuario;
import cl.duoc.grandline.backend.repository.BoletaRepository;
import cl.duoc.grandline.backend.repository.ProductoRepository;
import cl.duoc.grandline.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class BoletaService {

    private final BoletaRepository boletaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Boleta generarCompra(String correoUsuario, CompraRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("El carrito no puede estar vacío.");
        }

        Boleta boleta = new Boleta();
        boleta.setNumeroOrden("#OP-" + ThreadLocalRandom.current().nextInt(100000, 999999));
        boleta.setFecha(LocalDateTime.now());
        boleta.setCalleEnvio(request.getCalleEnvio());
        boleta.setDeptoEnvio(request.getDeptoEnvio());
        boleta.setComunaEnvio(request.getComunaEnvio());
        boleta.setUsuario(usuario);

        int totalCalculado = 0;

        for (CompraRequest.ItemCompraDTO itemDto : request.getItems()) {
            Producto producto = productoRepository.findById(itemDto.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado ID: " + itemDto.getProductoId()));

            if (producto.getStock() < itemDto.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para: " + producto.getNombre());
            }

            // Descontar stock en la base de datos
            producto.setStock(producto.getStock() - itemDto.getCantidad());
            productoRepository.save(producto);

            int subtotal = producto.getPrecio() * itemDto.getCantidad();
            totalCalculado += subtotal;

            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setBoleta(boleta);
            detalle.setProducto(producto);
            detalle.setCantidad(itemDto.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(subtotal);

            boleta.getDetalles().add(detalle);
        }

        boleta.setTotal(totalCalculado);
        return boletaRepository.save(boleta);
    }

    public List<Boleta> listarTodas() {
        return boletaRepository.findAllByOrderByFechaDesc();
    }

    public Boleta obtenerPorId(Long id) {
        return boletaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Boleta no encontrada ID: " + id));
    }

    public List<Boleta> listarPorUsuario(String correo) {
        return boletaRepository.findByUsuarioCorreoOrderByFechaDesc(correo.toLowerCase());
    }
}