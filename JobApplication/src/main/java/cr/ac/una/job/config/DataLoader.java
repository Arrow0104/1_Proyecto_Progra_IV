package cr.ac.una.job.config;

import cr.ac.una.job.models.*;
import cr.ac.una.job.repositories.*;
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
            IPuestoRepository puestoRepository,
            ICaracteristicaRepository caracteristicaRepository,
            IPuestoCaracteristicaRepository puestoCaracteristicaRepository) {

        return args -> {
            // 1) Usuarios/Empresas/Oferentes: si no hay usuarios
            if (usuarioRepository.count() == 0) {
                Usuario u1 = new Usuario(null, "empresa1@jobapp.com", "1234567890", "password123",
                        Rol.EMPRESA, EstadoUsuario.ACTIVO, true, LocalDateTime.now());
                Usuario u2 = new Usuario(null, "empresa2@jobapp.com", "0987654321", "password123",
                        Rol.EMPRESA, EstadoUsuario.ACTIVO, true, LocalDateTime.now());
                Usuario u3 = new Usuario(null, "oferente1@jobapp.com", "1111111111", "password123",
                        Rol.OFERENTE, EstadoUsuario.ACTIVO, true, LocalDateTime.now());
                Usuario u4 = new Usuario(null, "oferente2@jobapp.com", "2222222222", "password123",
                        Rol.OFERENTE, EstadoUsuario.ACTIVO, true, LocalDateTime.now());
                Usuario u5 = new Usuario(null, "admin@jobapp.com", "5555555555", "admin123",
                        Rol.ADMIN, EstadoUsuario.ACTIVO, true, LocalDateTime.now());

                usuarioRepository.save(u1);
                usuarioRepository.save(u2);
                usuarioRepository.save(u3);
                usuarioRepository.save(u4);
                usuarioRepository.save(u5);

                Empresa e1 = new Empresa(null, "Tech Solutions Costa Rica", "+506 2234-5678", true, LocalDateTime.now(), u1);
                Empresa e2 = new Empresa(null, "Digital Innovations Ltd", "+506 2765-4321", true, LocalDateTime.now(), u2);
                Empresa e3 = new Empresa(null, "CloudTech Services", "+506 8765-4321", true, LocalDateTime.now(), null);

                empresaRepository.save(e1);
                empresaRepository.save(e2);
                empresaRepository.save(e3);

                Oferente o1 = new Oferente(null, "Juan Pérez García", "San José", "/cv/juan_perez.pdf", true, LocalDateTime.now(), u3);
                Oferente o2 = new Oferente(null, "María López Rodríguez", "Heredia", "/cv/maria_lopez.pdf", true, LocalDateTime.now(), u4);
                Oferente o3 = new Oferente(null, "Carlos Méndez Soto", "Cartago", "/cv/carlos_mendez.pdf", true, LocalDateTime.now(), null);

                oferenteRepository.save(o1);
                oferenteRepository.save(o2);
                oferenteRepository.save(o3);
            }

            if (caracteristicaRepository.count() == 0) {

                // ── Raíces ───────────────────────────────────────────────────
                Caracteristica prog      = new Caracteristica(null, "Programación",   null);
                Caracteristica bd        = new Caracteristica(null, "Bases de Datos", null);
                Caracteristica devops    = new Caracteristica(null, "DevOps",         null);
                Caracteristica diseno    = new Caracteristica(null, "Diseño",         null);
                Caracteristica blandas   = new Caracteristica(null, "Habilidades Blandas", null);

                caracteristicaRepository.save(prog);
                caracteristicaRepository.save(bd);
                caracteristicaRepository.save(devops);
                caracteristicaRepository.save(diseno);
                caracteristicaRepository.save(blandas);

                // ── Programación ─────────────────────────────────────────────
                caracteristicaRepository.save(new Caracteristica(null, "Java",        prog));
                caracteristicaRepository.save(new Caracteristica(null, "Python",      prog));
                caracteristicaRepository.save(new Caracteristica(null, "JavaScript",  prog));
                caracteristicaRepository.save(new Caracteristica(null, "TypeScript",  prog));
                caracteristicaRepository.save(new Caracteristica(null, "C#",          prog));
                caracteristicaRepository.save(new Caracteristica(null, "Go",          prog));
                caracteristicaRepository.save(new Caracteristica(null, "Spring Boot", prog));
                caracteristicaRepository.save(new Caracteristica(null, "React",       prog));
                caracteristicaRepository.save(new Caracteristica(null, "Angular",     prog));
                caracteristicaRepository.save(new Caracteristica(null, "Node.js",     prog));

                // ── Bases de Datos ───────────────────────────────────────────
                caracteristicaRepository.save(new Caracteristica(null, "PostgreSQL",  bd));
                caracteristicaRepository.save(new Caracteristica(null, "MySQL",       bd));
                caracteristicaRepository.save(new Caracteristica(null, "MongoDB",     bd));
                caracteristicaRepository.save(new Caracteristica(null, "Redis",       bd));
                caracteristicaRepository.save(new Caracteristica(null, "Oracle",      bd));

                // ── DevOps ───────────────────────────────────────────────────
                caracteristicaRepository.save(new Caracteristica(null, "Docker",      devops));
                caracteristicaRepository.save(new Caracteristica(null, "Kubernetes",  devops));
                caracteristicaRepository.save(new Caracteristica(null, "Jenkins",     devops));
                caracteristicaRepository.save(new Caracteristica(null, "GitHub Actions", devops));
                caracteristicaRepository.save(new Caracteristica(null, "AWS",         devops));
                caracteristicaRepository.save(new Caracteristica(null, "Azure",       devops));

                // ── Diseño ───────────────────────────────────────────────────
                caracteristicaRepository.save(new Caracteristica(null, "Figma",       diseno));
                caracteristicaRepository.save(new Caracteristica(null, "Photoshop",   diseno));
                caracteristicaRepository.save(new Caracteristica(null, "Illustrator", diseno));
                caracteristicaRepository.save(new Caracteristica(null, "UX Research", diseno));

                // ── Habilidades Blandas ──────────────────────────────────────
                caracteristicaRepository.save(new Caracteristica(null, "Trabajo en equipo",  blandas));
                caracteristicaRepository.save(new Caracteristica(null, "Liderazgo",          blandas));
                caracteristicaRepository.save(new Caracteristica(null, "Comunicación",       blandas));
                caracteristicaRepository.save(new Caracteristica(null, "Resolución de problemas", blandas));
                caracteristicaRepository.save(new Caracteristica(null, "Gestión del tiempo", blandas));
            }

            // ── 3) Puestos + características ─────────────────────────────────
            if (puestoRepository.count() == 0) {
                Empresa e1 = empresaRepository.findAll().stream().findFirst().orElse(null);
                Empresa e2 = empresaRepository.findAll().stream().skip(1).findFirst().orElse(e1);
                Empresa e3 = empresaRepository.findAll().stream().skip(2).findFirst().orElse(e1);

                Puesto p1 = puestoRepository.save(new Puesto(null, "Desarrollador Java Senior",
                        "Buscamos desarrollador Java con experiencia en Spring Boot, microservicios y arquitectura de sistemas. Mínimo 5 años de experiencia.",
                        new BigDecimal("2500000"), EstadoPuesto.ACTIVO, true, LocalDateTime.now(), e1));

                Puesto p2 = puestoRepository.save(new Puesto(null, "Diseñador UI/UX",
                        "Profesional en diseño de interfaces y experiencia de usuario. Conocimiento en Figma, prototipos y diseño responsivo.",
                        new BigDecimal("1800000"), EstadoPuesto.ACTIVO, true, LocalDateTime.now(), e1));

                Puesto p3 = puestoRepository.save(new Puesto(null, "Analista de Sistemas",
                        "Análisis, diseño y mejora de procesos de sistemas. Experiencia en metodologías ágiles.",
                        new BigDecimal("2000000"), EstadoPuesto.ACTIVO, true, LocalDateTime.now(), e2));

                Puesto p4 = puestoRepository.save(new Puesto(null, "Desarrollador Full Stack",
                        "Desarrollador con experiencia en Java backend y React frontend. Trabajo con bases de datos relacionales.",
                        new BigDecimal("2200000"), EstadoPuesto.ACTIVO, true, LocalDateTime.now(), e2));

                Puesto p5 = puestoRepository.save(new Puesto(null, "Especialista en DevOps",
                        "Experiencia en Docker, Kubernetes, CI/CD pipelines y administración de infraestructura en la nube.",
                        new BigDecimal("2700000"), EstadoPuesto.ACTIVO, true, LocalDateTime.now(), e3));

                Puesto p6 = puestoRepository.save(new Puesto(null, "Líder de Proyecto",
                        "Coordinación y liderazgo de equipos de desarrollo. Experiencia en gestión de proyectos ágiles.",
                        new BigDecimal("3000000"), EstadoPuesto.ACTIVO, true, LocalDateTime.now(), e3));

                // Asignar características buscando por nombre (IDs seguros sin hardcodear)
                link(puestoCaracteristicaRepository, caracteristicaRepository, p1, "Java",                    4);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p1, "Spring Boot",             4);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p1, "PostgreSQL",              3);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p1, "Trabajo en equipo",       3);

                link(puestoCaracteristicaRepository, caracteristicaRepository, p2, "Figma",                   4);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p2, "Photoshop",               3);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p2, "UX Research",             3);

                link(puestoCaracteristicaRepository, caracteristicaRepository, p3, "Trabajo en equipo",       3);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p3, "Liderazgo",               2);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p3, "Resolución de problemas", 3);

                link(puestoCaracteristicaRepository, caracteristicaRepository, p4, "Java",                    3);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p4, "React",                   3);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p4, "PostgreSQL",              3);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p4, "Node.js",                 2);

                link(puestoCaracteristicaRepository, caracteristicaRepository, p5, "Docker",                  4);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p5, "Kubernetes",              4);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p5, "AWS",                     3);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p5, "GitHub Actions",          3);

                link(puestoCaracteristicaRepository, caracteristicaRepository, p6, "Liderazgo",               4);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p6, "Gestión del tiempo",      3);
                link(puestoCaracteristicaRepository, caracteristicaRepository, p6, "Comunicación",            4);
            }
        };
    }

    /** Busca la característica por nombre y la vincula al puesto con el nivel dado */
    private void link(IPuestoCaracteristicaRepository pcRepo,
                      ICaracteristicaRepository carRepo,
                      Puesto puesto, String nombre, int nivel) {
        carRepo.findByNombre(nombre).ifPresent(car ->
                pcRepo.save(new PuestoCaracteristica(puesto, car, nivel)));
    }
}