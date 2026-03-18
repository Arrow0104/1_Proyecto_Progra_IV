package cr.ac.una.job.auth;

import cr.ac.una.job.models.Rol;
import cr.ac.una.job.models.Usuario;
import jakarta.servlet.http.HttpSession;

public final class SessionAuth {

    private SessionAuth() {}

    public static Usuario getUsuario(HttpSession session) {
        Object u = session.getAttribute("usuario");
        return (u instanceof Usuario usuario) ? usuario : null;
    }

    public static Rol getRol(HttpSession session) {
        Object r = session.getAttribute("rol");
        return (r instanceof Rol rol) ? rol : null;
    }

    public static boolean isLogged(HttpSession session) {
        return getUsuario(session) != null;
    }

    public static boolean hasRole(HttpSession session, Rol rol) {
        Rol r = getRol(session);
        return r != null && r == rol;
    }
}
