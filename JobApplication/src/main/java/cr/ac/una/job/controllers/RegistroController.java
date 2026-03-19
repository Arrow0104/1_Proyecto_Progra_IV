package cr.ac.una.job.controllers;

import cr.ac.una.job.models.Empresa;
import cr.ac.una.job.models.EstadoUsuario;
import cr.ac.una.job.models.Oferente;
import cr.ac.una.job.models.Rol;
import cr.ac.una.job.models.Usuario;
import cr.ac.una.job.repositories.IEmpresaRepository;
import cr.ac.una.job.repositories.IOferenteRepository;
import cr.ac.una.job.repositories.IUsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class RegistroController {

    private static final Logger log = LoggerFactory.getLogger(RegistroController.class);

    private final IUsuarioRepository usuarioRepository;
    private final IEmpresaRepository empresaRepository;
    private final IOferenteRepository oferenteRepository;

    public RegistroController(IUsuarioRepository usuarioRepository,
                              IEmpresaRepository empresaRepository,
                              IOferenteRepository oferenteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.oferenteRepository = oferenteRepository;
    }

    // ── Registro de Empresa ──────────────────────────────────────────────────

    @Transactional
    @PostMapping("/registro/empresa")
    public String registrarEmpresa(
            @RequestParam String nombre,
            @RequestParam String telefono,
            @RequestParam String correo,
            @RequestParam(required = false, defaultValue = "") String localizacion,
            @RequestParam(required = false, defaultValue = "") String descripcion,
            @RequestParam String password,
            Model model) {

        String correoNorm = correo.trim().toLowerCase();

        if (usuarioRepository.findByCorreo(correoNorm).isPresent()) {
            model.addAttribute("error", "Ya existe una cuenta con ese correo electrónico.");
            return "public/registro-empresa";
        }

        // Identificación única: correo como base, con fallback si ya existe
        String identificacion = correoNorm;
        if (usuarioRepository.findByIdentificacion(identificacion).isPresent()) {
            identificacion = "emp_" + System.currentTimeMillis();
        }

        Usuario usuario = new Usuario(
                null, correoNorm, identificacion, password,
                Rol.EMPRESA, EstadoUsuario.PENDIENTE, true, LocalDateTime.now()
        );
        usuario = usuarioRepository.save(usuario);
        log.info("Empresa usuario creado id={} correo={}", usuario.getIdUsuario(), correoNorm);

        Empresa empresa = new Empresa(
                null, nombre.trim(), telefono.trim(), true, LocalDateTime.now(), usuario
        );
        empresaRepository.save(empresa);
        log.info("Empresa creada para usuario id={}", usuario.getIdUsuario());

        return "redirect:/login?msg=Empresa%20registrada.%20Espere%20la%20aprobaci%C3%B3n%20del%20administrador.";
    }

    // ── Registro de Oferente ─────────────────────────────────────────────────

    @Transactional
    @PostMapping("/registro/oferente")
    public String registrarOferente(
            @RequestParam String nombre,
            @RequestParam(required = false, defaultValue = "") String apellido,
            @RequestParam String correo,
            @RequestParam(required = false, defaultValue = "") String numIdentificacion,
            @RequestParam(required = false, defaultValue = "") String nacionalidad,
            @RequestParam(required = false, defaultValue = "") String telefono,
            @RequestParam(required = false, defaultValue = "") String residencia,
            @RequestParam String password,
            Model model) {

        String correoNorm = correo.trim().toLowerCase();

        // Validar correo único
        if (usuarioRepository.findByCorreo(correoNorm).isPresent()) {
            model.addAttribute("error", "Ya existe una cuenta con ese correo electrónico.");
            return "public/registro-oferente";
        }

        // Determinar identificación única
        String identificacion = numIdentificacion.isBlank() ? correoNorm : numIdentificacion.trim();
        if (usuarioRepository.findByIdentificacion(identificacion).isPresent()) {
            if (!numIdentificacion.isBlank()) {
                model.addAttribute("error", "Ya existe una cuenta con esa identificación.");
                return "public/registro-oferente";
            }
            identificacion = "of_" + System.currentTimeMillis();
        }

        // Nombre completo
        String nombreCompleto = nombre.trim();
        if (!apellido.isBlank()) {
            nombreCompleto = nombreCompleto + " " + apellido.trim();
        }

        // 1. Guardar usuario y obtener el ID asignado por la BD
        Usuario usuario = new Usuario(
                null, correoNorm, identificacion, password,
                Rol.OFERENTE, EstadoUsuario.PENDIENTE, true, LocalDateTime.now()
        );
        usuario = usuarioRepository.save(usuario);
        log.info("Oferente usuario creado id={} correo={}", usuario.getIdUsuario(), correoNorm);

        // 2. Crear oferente con referencia al usuario ya persistido
        Oferente oferente = new Oferente(
                null,
                nombreCompleto,
                residencia.isBlank() ? "No especificada" : residencia.trim(),
                "",         // cvPath vacío — se sube luego desde el dashboard
                true,
                LocalDateTime.now(),
                usuario     // referencia directa al objeto con ID ya generado
        );
        Oferente saved = oferenteRepository.save(oferente);
        log.info("Oferente creado id={} para usuario id={}", saved.getIdOferente(), usuario.getIdUsuario());

        return "redirect:/login?msg=Oferente%20registrado.%20Espere%20la%20aprobaci%C3%B3n%20del%20administrador.";
    }
}
