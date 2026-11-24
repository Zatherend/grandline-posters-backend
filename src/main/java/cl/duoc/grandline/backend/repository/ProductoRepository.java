package cl.duoc.grandline.backend.repository;

import cl.duoc.grandline.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> findByStockLessThanEqual(Integer stock);
}