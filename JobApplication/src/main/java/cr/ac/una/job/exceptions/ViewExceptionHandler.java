package cr.ac.una.job.exceptions;

import cr.ac.una.job.controllers.ProductViewController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {ProductViewController.class})
public class ViewExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFoundException(ProductNotFoundException ex, Model model) {
        model.addAttribute("pageTitle", "Product Not Found");
        model.addAttribute("errorTitle", "Product Not Found");
        model.addAttribute("errorMessage", ex.getMessage());

        return "products/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("pageTitle", "Error");
        model.addAttribute("errorTitle", "An Error Occurred");
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");

        return "products/error";
    }
}
