package cl.duoc.grandline.backend.repository;

import cl.duoc.grandline.backend.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    List<Boleta> findByUsuarioCorreoOrderByFechaDesc(String correo);
    List<Boleta> findAllByOrderByFechaDesc();
}