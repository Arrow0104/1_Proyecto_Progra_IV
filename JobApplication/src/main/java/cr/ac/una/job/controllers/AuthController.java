package cr.ac.una.job.controllers;

import cr.ac.una.job.models.Usuario;
import cr.ac.una.job.repositories.IUsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    private final IUsuarioRepository usuarioRepository;

    public AuthController(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam(required = false) String username,
                          @RequestParam(required = false) String password,
                          HttpSession session) {

        String user = (username == null) ? "" : username.trim();
        String pass = (password == null) ? "" : password;

        if (user.isBlank() || pass.isBlank()) {
            return "redirect:/login?error=Debe%20ingresar%20usuario%20y%20contraseña";
        }

        Optional<Usuario> opt = usuarioRepository.findByCorreo(user);
        if (opt.isEmpty()) opt = usuarioRepository.findByIdentificacion(user);

        if (opt.isEmpty()) return "redirect:/login?error=Credenciales%20inv%C3%A1lidas";

        Usuario u = opt.get();

        if (!pass.equals(u.getPassword())) return "redirect:/login?error=Credenciales%20inv%C3%A1lidas";
        if (!u.isActive()) return "redirect:/login?error=Usuario%20inactivo";

        session.setAttribute("usuario", u);
        session.setAttribute("rol", u.getRol());

        return switch (u.getRol()) {
            case ADMIN -> "redirect:/usuarios";
            case EMPRESA -> "redirect:/empresas";
            case OFERENTE -> "redirect:/oferentes";
        };
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/inicio?msg=Sesión%20cerrada";
    }
}