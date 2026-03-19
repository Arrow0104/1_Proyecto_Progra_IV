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
        // Recursos estáticos
        if (startsWith(uri, "/css") || startsWith(uri, "/js")
                || startsWith(uri, "/images") || startsWith(uri, "/webjars")) {
            return true;
        }

        // Error page — MUY IMPORTANTE dejarla pública
        if (uri.equals("/error")) return true;

        // Páginas públicas de navegación
        if (uri.equals("/") || uri.equals("/inicio")
                || uri.equals("/login")
                || uri.equals("/register-empresa")
                || uri.equals("/register-oferente")) {
            return true;
        }

        // Rutas de registro (POST y GET)
        if (startsWith(uri, "/registro/")) return true;

        // Búsqueda y detalle de puestos — acceso público
        if (startsWith(uri, "/puestos/")) return true;

        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // Rutas públicas
        if (isPublicPath(uri)) return true;

        // Permitir POST /login
        if (uri.equals("/login") && "POST".equalsIgnoreCase(request.getMethod())) return true;

        // Permitir POST /logout
        if (uri.equals("/logout") && "POST".equalsIgnoreCase(request.getMethod())) return true;

        HttpSession session = request.getSession(false);
        Usuario usuario = (session == null) ? null : (Usuario) session.getAttribute("usuario");
        Rol rol = (session == null) ? null : (Rol) session.getAttribute("rol");

        // Sin sesión → a login
        if (usuario == null || rol == null) {
            response.sendRedirect("/login?error=Debe%20iniciar%20sesión");
            return false;
        }

        // /admin/** → solo ADMIN
        if (startsWith(uri, "/admin") && rol != Rol.ADMIN) {
            response.sendRedirect("/inicio?error=No%20autorizado");
            return false;
        }

        // /empresas/** → solo EMPRESA
        if (startsWith(uri, "/empresas") && rol != Rol.EMPRESA) {
            response.sendRedirect("/inicio?error=No%20autorizado");
            return false;
        }

        // /oferentes/** → solo OFERENTE
        if (startsWith(uri, "/oferentes") && rol != Rol.OFERENTE) {
            response.sendRedirect("/inicio?error=No%20autorizado");
            return false;
        }

        // /usuarios/** → solo ADMIN
        if (startsWith(uri, "/usuarios") && rol != Rol.ADMIN) {
            response.sendRedirect("/inicio?error=No%20autorizado");
            return false;
        }

        return true;
    }
}
