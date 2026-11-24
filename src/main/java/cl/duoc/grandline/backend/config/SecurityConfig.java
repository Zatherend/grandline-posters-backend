package cl.duoc.grandline.backend.config;

import cl.duoc.grandline.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (Autenticación, Swagger y consulta del catálogo)
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/productos/**", "/api/v2/productos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categorias/**").permitAll()

                        // Rutas de Compra y Perfil (Usuarios autenticados)
                        .requestMatchers(HttpMethod.POST, "/api/v1/boletas").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/boletas/mis-compras").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/perfil").authenticated()

                        // Rutas de Órdenes (ADMIN y VENDEDOR pueden listar y ver detalle)
                        .requestMatchers(HttpMethod.GET, "/api/v1/boletas", "/api/v1/boletas/**").hasAnyRole("ADMIN", "VENDEDOR")

                        // Rutas exclusivas de ADMINISTRADOR (CRUD Productos, Categorías y Usuarios)
                        .requestMatchers(HttpMethod.POST, "/api/v1/productos/**", "/api/v2/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**", "/api/v2/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**", "/api/v2/productos/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/categorias/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/usuarios/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}