package cr.ac.una.job.exceptions;

import cr.ac.una.job.controllers.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {
        ProductViewController.class,
        EmpresaViewController.class,
        PuestoViewController.class,
        OferenteViewController.class,
        UsuarioViewController.class
})
public class ViewExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFoundException(ProductNotFoundException ex, Model model) {
        model.addAttribute("pageTitle", "Product Not Found");
        model.addAttribute("errorTitle", "Product Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "products/error";
    }

    @ExceptionHandler(EmpresaNotFoundException.class)
    public String handleEmpresaNotFoundException(EmpresaNotFoundException ex, Model model) {
        model.addAttribute("pageTitle", "Empresa no encontrada");
        model.addAttribute("errorTitle", "Empresa no encontrada");
        model.addAttribute("errorMessage", ex.getMessage());
        return "empresas/error";
    }

    @ExceptionHandler(PuestoNotFoundException.class)
    public String handlePuestoNotFoundException(PuestoNotFoundException ex, Model model) {
        model.addAttribute("pageTitle", "Puesto no encontrado");
        model.addAttribute("errorTitle", "Puesto no encontrado");
        model.addAttribute("errorMessage", ex.getMessage());
        return "puestos/error";
    }

    @ExceptionHandler(OferenteNotFoundException.class)
    public String handleOferenteNotFoundException(OferenteNotFoundException ex, Model model) {
        model.addAttribute("pageTitle", "Oferente no encontrado");
        model.addAttribute("errorTitle", "Oferente no encontrado");
        model.addAttribute("errorMessage", ex.getMessage());
        return "oferentes/error";
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public String handleUsuarioNotFoundException(UsuarioNotFoundException ex, Model model) {
        model.addAttribute("pageTitle", "Usuario no encontrado");
        model.addAttribute("errorTitle", "Usuario no encontrado");
        model.addAttribute("errorMessage", ex.getMessage());
        return "usuarios/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("pageTitle", "Error");
        model.addAttribute("errorTitle", "Ocurrió un error");
        model.addAttribute("errorMessage", "Ocurrió un error inesperado. Intente de nuevo.");
        return "products/error";
    }
}
