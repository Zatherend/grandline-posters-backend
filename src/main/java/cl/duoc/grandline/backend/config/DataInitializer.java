package cl.duoc.grandline.backend.config;

import cl.duoc.grandline.backend.model.Categoria;
import cl.duoc.grandline.backend.model.Producto;
import cl.duoc.grandline.backend.model.Usuario;
import cl.duoc.grandline.backend.repository.CategoriaRepository;
import cl.duoc.grandline.backend.repository.ProductoRepository;
import cl.duoc.grandline.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.saveAll(List.of(
                    new Usuario(null, "Administrador", "Marina HQ", "admin@duoc.cl",
                            passwordEncoder.encode("admin123"), "ADMIN", "Cuartel General Marineford 1", "Oficina Almirante", "Santiago"),
                    new Usuario(null, "Vendedor", "Grand Line", "vendedor@duoc.cl",
                            passwordEncoder.encode("vendedor123"), "VENDEDOR", "Av. Providencia 1234", "Local 5", "Providencia"),
                    new Usuario(null, "Maximiliano", "Álvarez", "maxi@duocuc.cl",
                            passwordEncoder.encode("123456"), "CLIENTE", "Av. Vicuña Mackenna 4917", "", "La Florida")
            ));
        }

        if (categoriaRepository.count() == 0) {
            Categoria leyendas = categoriaRepository.save(new Categoria(null, "Leyendas", "Piratas legendarios de la vieja era"));
            Categoria yonkou = categoriaRepository.save(new Categoria(null, "Yonkou", "Cuatro Emperadores del Mar"));
            Categoria crossGuild = categoriaRepository.save(new Categoria(null, "Cross Guild", "Organización caza-marines"));
            Categoria yonkouCross = categoriaRepository.save(new Categoria(null, "Yonkou / Cross Guild", "Emperador y líder de Cross Guild"));
            Categoria mugiwara = categoriaRepository.save(new Categoria(null, "Mugiwara", "Tripulación de los Sombrero de Paja"));
            Categoria supernova = categoriaRepository.save(new Categoria(null, "Supernova", "La Peor Generación"));
            Categoria kuja = categoriaRepository.save(new Categoria(null, "Kuja", "Piratas Kuja de Amazon Lily"));
            Categoria barbablanca = categoriaRepository.save(new Categoria(null, "Barbablanca", "Comandantes de Barbablanca"));

            productoRepository.saveAll(List.of(
                    new Producto(null, "Wanted: Gol D. Roger", 5564800000L, 9990, 2, "/src/img/roger.jpg", "El Rey de los Piratas. Edición especial dorada.", leyendas),
                    new Producto(null, "Wanted: Edward Newgate (Barbablanca)", 5046000000L, 8990, 3, "/src/img/barbablanca.jpg", "El hombre más fuerte del mundo.", leyendas),
                    new Producto(null, "Wanted: Kaido de las Bestias", 4611100000L, 7990, 6, "/src/img/kaido.jpg", "Capitán de los Piratas Bestia y Gobernador de Onigashima.", yonkou),
                    new Producto(null, "Wanted: Charlotte Linlin (Big Mom)", 4388000000L, 7990, 7, "/src/img/linlin.jpg", "Reina de Totto Land y capitana de los Piratas de Big Mom.", yonkou),
                    new Producto(null, "Wanted: Shanks el Pelirrojo", 4048900000L, 8490, 4, "/src/img/shanks.jpg", "Jefe de los Piratas del Pelirrojo.", yonkou),
                    new Producto(null, "Wanted: Marshall D. Teach (Barbanegra)", 3996000000L, 7490, 8, "/src/img/barbanegra.jpg", "Almirante de los Piratas de Barbanegra.", yonkou),
                    new Producto(null, "Wanted: Dracule Mihawk", 3590000000L, 7490, 5, "/src/img/mihawk.jpg", "El mejor espadachín del mundo.", crossGuild),
                    new Producto(null, "Wanted: Buggy el Payaso Estrella", 3189000000L, 6490, 14, "/src/img/buggy.jpg", "Líder figura de Cross Guild y Emperador del Mar.", yonkouCross),
                    new Producto(null, "Wanted: Monkey D. Luffy (Gear 5)", 3000000000L, 6990, 3, "/src/img/luffy.jpg", "Capitán de los Sombrero de Paja y Emperador del Mar.", mugiwara),
                    new Producto(null, "Wanted: Roronoa Zoro", 1111000000L, 5990, 10, "/src/img/zoro.jpeg", "El Cazador de Piratas y combatiente principal.", mugiwara),
                    new Producto(null, "Wanted: Jinbe", 1100000000L, 5490, 8, "/src/img/jinbe.jpg", "El Caballero del Mar, timonel de la banda.", mugiwara),
                    new Producto(null, "Wanted: Vinsmoke Sanji", 1032000000L, 5990, 4, "/src/img/sanji.png", "Pierna Negra, cocinero de los Sombrero de Paja.", mugiwara),
                    new Producto(null, "Wanted: Nico Robin", 930000000L, 5490, 2, "/src/img/robin.jpg", "La Niña Demonio, arqueóloga de la tripulación.", mugiwara),
                    new Producto(null, "Wanted: God Usopp", 500000000L, 4990, 15, "/src/img/usopp.jpg", "El francotirador de los Sombrero de Paja.", mugiwara),
                    new Producto(null, "Wanted: Franky", 394000000L, 4490, 9, "/src/img/franky.jpg", "Carpintero cyborg del Thousand Sunny.", mugiwara),
                    new Producto(null, "Wanted: Brook", 383000000L, 4490, 5, "/src/img/brook.jpeg", "Soul King, músico de la banda.", mugiwara),
                    new Producto(null, "Wanted: Nami", 366000000L, 4990, 12, "/src/img/nami.jpg", "La Gata Ladrona, navegante oficial.", mugiwara),
                    new Producto(null, "Wanted: Tony Tony Chopper", 1000L, 2990, 25, "/src/img/chopper.jpg", "Médico de abordo.", mugiwara),
                    new Producto(null, "Wanted: Trafalgar D. Water Law", 3000000000L, 6490, 6, "/src/img/law.jpeg", "El Cirujano de la Muerte, capitán de los Piratas Heart.", supernova),
                    new Producto(null, "Wanted: Eustass Kid", 3000000000L, 6490, 7, "/src/img/kid.jpg", "Capitán de los Piratas de Kid.", supernova),
                    new Producto(null, "Wanted: Boa Hancock", 1659000000L, 5990, 4, "/src/img/hancock.png", "La Emperatriz Pirata de Amazon Lily.", kuja),
                    new Producto(null, "Wanted: Portgas D. Ace", 550000000L, 6990, 3, "/src/img/ace.jpg", "Puño de Fuego, comandante de la 2da división.", barbablanca)
            ));
        }
    }
}