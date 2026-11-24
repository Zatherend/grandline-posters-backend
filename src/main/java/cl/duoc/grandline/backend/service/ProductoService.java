package cl.duoc.grandline.backend.service;

import cl.duoc.grandline.backend.dto.ProductoRequest;
import cl.duoc.grandline.backend.model.Categoria;
import cl.duoc.grandline.backend.model.Producto;
import cl.duoc.grandline.backend.repository.CategoriaRepository;
import cl.duoc.grandline.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    public List<Producto> listarCriticos() {
        return productoRepository.findByStockLessThanEqual(5);
    }

    public Producto crear(ProductoRequest request) {
        Categoria categoria = obtenerOCrearCategoria(request.getCategoria());

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setRecompensa(request.getRecompensa() != null ? request.getRecompensa() : 0L);
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setImagen(request.getImagen() != null && !request.getImagen().isBlank()
                ? request.getImagen() : "/src/img/luffy.jpg");
        producto.setDescripcion(request.getDescripcion());
        producto.setCategoria(categoria);

        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, ProductoRequest request) {
        Producto existente = obtenerPorId(id);

        if (request.getNombre() != null) existente.setNombre(request.getNombre());
        if (request.getRecompensa() != null) existente.setRecompensa(request.getRecompensa());
        if (request.getPrecio() != null) existente.setPrecio(request.getPrecio());
        if (request.getStock() != null) existente.setStock(request.getStock());
        if (request.getImagen() != null) existente.setImagen(request.getImagen());
        if (request.getDescripcion() != null) existente.setDescripcion(request.getDescripcion());
        if (request.getCategoria() != null && !request.getCategoria().isBlank()) {
            existente.setCategoria(obtenerOCrearCategoria(request.getCategoria()));
        }

        return productoRepository.save(existente);
    }

    public void eliminar(Long id) {
        Producto existente = obtenerPorId(id);
        productoRepository.delete(existente);
    }

    private Categoria obtenerOCrearCategoria(String nombreCategoria) {
        String nombreLimpio = (nombreCategoria == null || nombreCategoria.isBlank()) ? "Mugiwara" : nombreCategoria.trim();
        return categoriaRepository.findByNombre(nombreLimpio)
                .orElseGet(() -> categoriaRepository.save(new Categoria(null, nombreLimpio, "Categoría " + nombreLimpio)));
    }
}