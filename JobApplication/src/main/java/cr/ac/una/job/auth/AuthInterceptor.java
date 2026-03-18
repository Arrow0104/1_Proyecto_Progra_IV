package cr.ac.una.job.auth;

import cr.ac.una.job.models.Rol;
import cr.ac.una.job.models.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    private static boolean startsWith(String uri, String prefix) {
        return uri != null && uri.startsWith(prefix);
    }

    private static boolean isPublicPath(String uri) {
        if (startsWith(uri, "/css") || startsWith(uri, "/js") || startsWith(uri, "/images") || startsWith(uri, "/webjars")) {
            return true;
        }

        // MUY IMPORTANTE: dejar /error público
        if (uri.equals("/error")) return true;

        return uri.equals("/") || uri.equals("/inicio") || uri.equals("/login")
                || uri.equals("/register-empresa") || uri.equals("/register-oferente");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // Permitir públicos
        if (isPublicPath(uri)) return true;

        // Permitir POST /login (tu login simple)
        if (uri.equals("/login") && "POST".equalsIgnoreCase(request.getMethod())) return true;

        // Permitir POST /logout (aunque esté sin sesión)
        if (uri.equals("/logout") && "POST".equalsIgnoreCase(request.getMethod())) return true;

        HttpSession session = request.getSession(false);
        Usuario usuario = (session == null) ? null : (Usuario) session.getAttribute("usuario");
        Rol rol = (session == null) ? null : (Rol) session.getAttribute("rol");

        // Si no hay sesión → a login
        if (usuario == null || rol == null) {
            response.sendRedirect("/login?error=Debe%20iniciar%20sesión");
            return false;
        }

        // Reglas por rol / prefijo
        if (startsWith(uri, "/empresas") && rol != Rol.EMPRESA) {
            response.sendRedirect("/inicio?error=No%20autorizado");
            return false;
        }

        if (startsWith(uri, "/oferentes") && rol != Rol.OFERENTE) {
            response.sendRedirect("/inicio?error=No%20autorizado");
            return false;
        }

        // Admin en este repo está colgado de /usuarios (renderiza admin/dashboard)
        if (startsWith(uri, "/usuarios") && rol != Rol.ADMIN) {
            response.sendRedirect("/inicio?error=No%20autorizado");
            return false;
        }

        // Si más adelante agregas /admin/**, puedes incluirlo aquí también.

        return true;
    }


}
