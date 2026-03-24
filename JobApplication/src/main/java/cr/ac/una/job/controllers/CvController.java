package cr.ac.una.job.controllers;

import cr.ac.una.job.models.Oferente;
import cr.ac.una.job.models.Usuario;
import cr.ac.una.job.repositories.IOferenteRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/oferente/cv")
public class CvController {

    private static final Logger log = LoggerFactory.getLogger(CvController.class);


    private static final String CV_DIR = "uploads/cv/";

    private final IOferenteRepository oferenteRepository;

    public CvController(IOferenteRepository oferenteRepository) {
        this.oferenteRepository = oferenteRepository;

        try {
            Files.createDirectories(Paths.get(CV_DIR));
        } catch (IOException e) {
            log.warn("No se pudo crear el directorio de CVs: {}", e.getMessage());
        }
    }

    private Oferente getOferenteFromSession(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return null;
        return oferenteRepository.findFirstByUsuarioIdUsuario(usuario.getIdUsuario()).orElse(null);
    }


    @GetMapping
    public String verCv(HttpSession session, Model model) {
        Oferente oferente = getOferenteFromSession(session);
        if (oferente == null) return "redirect:/login?error=Sesión%20no%20válida";

        model.addAttribute("oferente", oferente);
        model.addAttribute("pageTitle", "Mi Currículo");
        return "oferentes/cv";
    }


    @Transactional
    @PostMapping("/subir")
    public String subirCv(@RequestParam("archivoCv") MultipartFile archivo,
                          HttpSession session) {
        Oferente oferente = getOferenteFromSession(session);
        if (oferente == null) return "redirect:/login?error=Sesión%20no%20válida";

        if (archivo.isEmpty()) {
            return "redirect:/oferente/cv?error=Debe%20seleccionar%20un%20archivo";
        }


        String contentType = archivo.getContentType();
        String originalName = archivo.getOriginalFilename();
        if (contentType == null || !contentType.equalsIgnoreCase("application/pdf")) {
            return "redirect:/oferente/cv?error=Solo%20se%20permiten%20archivos%20PDF";
        }

        try {

            String extension = ".pdf";
            String nombreArchivo = "cv_" + oferente.getIdOferente()
                    + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;

            Path destino = Paths.get(CV_DIR + nombreArchivo);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);


            String rutaRelativa = "/cv/" + nombreArchivo;
            oferente.setCvPath(rutaRelativa);
            oferenteRepository.save(oferente);

            log.info("CV subido para oferente={}: {}", oferente.getIdOferente(), rutaRelativa);
            return "redirect:/oferente/cv?msg=CV%20subido%20correctamente";

        } catch (IOException e) {
            log.error("Error al subir CV para oferente={}: {}", oferente.getIdOferente(), e.getMessage());
            return "redirect:/oferente/cv?error=Error%20al%20subir%20el%20archivo";
        }
    }


    @Transactional
    @PostMapping("/guardar")
    public String guardarRuta(@RequestParam String cvPath, HttpSession session) {
        Oferente oferente = getOferenteFromSession(session);
        if (oferente == null) return "redirect:/login?error=Sesión%20no%20válida";

        oferente.setCvPath(cvPath == null ? "" : cvPath.trim());
        oferenteRepository.save(oferente);

        log.info("Ruta CV actualizada para oferente={}", oferente.getIdOferente());
        return "redirect:/oferente/cv?msg=CV%20actualizado%20correctamente";
    }
}