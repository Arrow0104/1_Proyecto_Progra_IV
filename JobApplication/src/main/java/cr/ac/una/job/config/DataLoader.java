package cr.ac.una.job.config;

import cr.ac.una.job.models.Empresa;
import cr.ac.una.job.models.EstadoPuesto;
import cr.ac.una.job.models.EstadoUsuario;
import cr.ac.una.job.models.Oferente;
import cr.ac.una.job.models.Puesto;
import cr.ac.una.job.models.Rol;
import cr.ac.una.job.models.Usuario;
import cr.ac.una.job.repositories.IEmpresaRepository;
import cr.ac.una.job.repositories.IOferenteRepository;
import cr.ac.una.job.repositories.IPuestoRepository;
import cr.ac.una.job.repositories.IUsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(
            IUsuarioRepository usuarioRepository,
            IEmpresaRepository empresaRepository,
            IOferenteRepository oferenteRepository,
            IPuestoRepository puestoRepository) {

        return args -> {
            if (usuarioRepository.count() == 0) {

                // Crear Usuarios
                Usuario u1 = new Usuario(
                        null,
                        "empresa1@jobapp.com",
                        "1234567890",
                        "password123",
                        Rol.EMPRESA,
                        EstadoUsuario.ACTIVO,
                        true,
                        LocalDateTime.now()
                );

                Usuario u2 = new Usuario(
                        null,
                        "empresa2@jobapp.com",
                        "0987654321",
                        "password123",
                        Rol.EMPRESA,
                        EstadoUsuario.ACTIVO,
                        true,
                        LocalDateTime.now()
                );

                Usuario u3 = new Usuario(
                        null,
                        "oferente1@jobapp.com",
                        "1111111111",
                        "password123",
                        Rol.OFERENTE,
                        EstadoUsuario.ACTIVO,
                        true,
                        LocalDateTime.now()
                );

                Usuario u4 = new Usuario(
                        null,
                        "oferente2@jobapp.com",
                        "2222222222",
                        "password123",
                        Rol.OFERENTE,
                        EstadoUsuario.ACTIVO,
                        true,
                        LocalDateTime.now()
                );

                Usuario u5 = new Usuario(
                        null,
                        "admin@jobapp.com",
                        "5555555555",
                        "admin123",
                        Rol.ADMIN,
                        EstadoUsuario.ACTIVO,
                        true,
                        LocalDateTime.now()
                );

                usuarioRepository.save(u1);
                usuarioRepository.save(u2);
                usuarioRepository.save(u3);
                usuarioRepository.save(u4);
                usuarioRepository.save(u5);

                // Crear Empresas
                Empresa e1 = new Empresa(
                        null,
                        "Tech Solutions Costa Rica",
                        "+506 2234-5678",
                        true,
                        LocalDateTime.now(),
                        u1
                );

                Empresa e2 = new Empresa(
                        null,
                        "Digital Innovations Ltd",
                        "+506 2765-4321",
                        true,
                        LocalDateTime.now(),
                        u2
                );

                Empresa e3 = new Empresa(
                        null,
                        "CloudTech Services",
                        "+506 8765-4321",
                        true,
                        LocalDateTime.now(),
                        null
                );

                empresaRepository.save(e1);
                empresaRepository.save(e2);
                empresaRepository.save(e3);

                // Crear Oferentes
                Oferente o1 = new Oferente(
                        null,
                        "Juan Pérez García",
                        "San José",
                        "/cv/juan_perez.pdf",
                        true,
                        LocalDateTime.now(),
                        u3
                );

                Oferente o2 = new Oferente(
                        null,
                        "María López Rodríguez",
                        "Heredia",
                        "/cv/maria_lopez.pdf",
                        true,
                        LocalDateTime.now(),
                        u4
                );

                Oferente o3 = new Oferente(
                        null,
                        "Carlos Méndez Soto",
                        "Cartago",
                        "/cv/carlos_mendez.pdf",
                        true,
                        LocalDateTime.now(),
                        null
                );

                oferenteRepository.save(o1);
                oferenteRepository.save(o2);
                oferenteRepository.save(o3);

                // Crear Puestos
                Puesto p1 = new Puesto(
                        null,
                        "Desarrollador Java Senior",
                        "Buscamos desarrollador Java con experiencia en Spring Boot, microservicios y arquitectura de sistemas. Mínimo 5 años de experiencia.",
                        new BigDecimal("2500000"),
                        EstadoPuesto.ACTIVO,
                        true,
                        LocalDateTime.now(),
                        e1
                );

                Puesto p2 = new Puesto(
                        null,
                        "Diseñador UI/UX",
                        "Profesional en diseño de interfaces y experiencia de usuario. Conocimiento en Figma, prototipos y diseño responsivo.",
                        new BigDecimal("1800000"),
                        EstadoPuesto.ACTIVO,
                        true,
                        LocalDateTime.now(),
                        e1
                );

                Puesto p3 = new Puesto(
                        null,
                        "Analista de Sistemas",
                        "Análisis, diseño y mejora de procesos de sistemas. Experiencia en metodologías ágiles.",
                        new BigDecimal("2000000"),
                        EstadoPuesto.ACTIVO,
                        true,
                        LocalDateTime.now(),
                        e2
                );

                Puesto p4 = new Puesto(
                        null,
                        "Desarrollador Full Stack",
                        "Desarrollador con experiencia en Java backend y React frontend. Trabajo con bases de datos relacionales.",
                        new BigDecimal("2200000"),
                        EstadoPuesto.ACTIVO,
                        true,
                        LocalDateTime.now(),
                        e2
                );

                Puesto p5 = new Puesto(
                        null,
                        "Especialista en DevOps",
                        "Experiencia en Docker, Kubernetes, CI/CD pipelines y administración de infraestructura en la nube.",
                        new BigDecimal("2700000"),
                        EstadoPuesto.ACTIVO,
                        true,
                        LocalDateTime.now(),
                        e3
                );

                Puesto p6 = new Puesto(
                        null,
                        "Líder de Proyecto",
                        "Coordinación y liderazgo de equipos de desarrollo. Experiencia en gestión de proyectos ágiles.",
                        new BigDecimal("3000000"),
                        EstadoPuesto.ACTIVO,
                        true,
                        LocalDateTime.now(),
                        e3
                );

                puestoRepository.save(p1);
                puestoRepository.save(p2);
                puestoRepository.save(p3);
                puestoRepository.save(p4);
                puestoRepository.save(p5);
                puestoRepository.save(p6);
            }
        };
    }
}
