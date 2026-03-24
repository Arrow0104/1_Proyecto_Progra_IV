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
        if (startsWith(uri, "/css") || startsWith(uri, "/js")
                || startsWith(uri, "/images") || startsWith(uri, "/webjars")
                || startsWith(uri, "/cv/"))
            return true;
        if (uri.equals("/error")) return true;
        if (uri.equals("/") || uri.equals("/inicio") || uri.equals("/login")
                || uri.equals("/register-empresa") || uri.equals("/register-oferente")) return true;
        if (startsWith(uri, "/registro/")) return true;
        if (startsWith(uri, "/puestos/"))  return true;
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri    = request.getRequestURI();
        String method = request.getMethod();

        if (isPublicPath(uri)) return true;
        if (uri.equals("/login")  && "POST".equalsIgnoreCase(method)) return true;
        if (uri.equals("/logout") && "POST".equalsIgnoreCase(method)) return true;

        HttpSession session = request.getSession(false);
        Usuario usuario = (session == null) ? null : (Usuario) session.getAttribute("usuario");
        Rol     rol     = (session == null) ? null : (Rol)     session.getAttribute("rol");

        if (usuario == null || rol == null) {
            response.sendRedirect("/login?error=Debe%20iniciar%20sesion");
            return false;
        }


        if ((startsWith(uri, "/admin") || startsWith(uri, "/usuarios"))
                && rol != Rol.ADMIN) {
            response.sendRedirect("/inicio?error=No%20autorizado");
            return false;
        }



        if ((startsWith(uri, "/empresas") || startsWith(uri, "/empresa"))
                && rol != Rol.EMPRESA) {
            response.sendRedirect("/inicio?error=No%20autorizado");
            return false;
        }


        if ((startsWith(uri, "/oferentes") || startsWith(uri, "/oferente"))
                && rol != Rol.OFERENTE) {
            response.sendRedirect("/inicio?error=No%20autorizado");
            return false;
        }

        return true;
    }
}