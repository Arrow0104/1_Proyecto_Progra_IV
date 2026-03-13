package cr.ac.una.job.controllers;

import cr.ac.una.job.dtos.product.CreateProductRequest;
import cr.ac.una.job.dtos.product.UpdateProductRequest;
import cr.ac.una.job.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductViewController {
    private final ProductService service;

    public ProductViewController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", service.getAllProducts());
        model.addAttribute("pageTitle", "Products List");

        return "products/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("product", service.getProductById(id));
        model.addAttribute("pageTitle", "Product Detail");

        return "products/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("productForm", new CreateProductRequest());
        model.addAttribute("pageTitle", "Create Product");
        model.addAttribute("formTitle", "Create New Product");
        model.addAttribute("formAction", "/products");
        model.addAttribute("isEdit", false);

        return "products/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("productForm") CreateProductRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Create Product");
            model.addAttribute("formTitle", "Create New Product");
            model.addAttribute("formAction", "/products");
            model.addAttribute("isEdit", false);

            return "products/form";
        }

        service.createProduct(request);

        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        UpdateProductRequest request = service.buildUpdateRequest(id);

        model.addAttribute("productForm", request);
        model.addAttribute("productId", id);
        model.addAttribute("pageTitle", "Edit Product");
        model.addAttribute("formTitle", "Edit Product");
        model.addAttribute("formAction", "/products/" + id);
        model.addAttribute("isEdit", true);

        return "products/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("productForm") UpdateProductRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            model.addAttribute("pageTitle", "Edit Product");
            model.addAttribute("formTitle", "Edit Product");
            model.addAttribute("formAction", "/products/" + id);
            model.addAttribute("isEdit", true);

            return "products/form";
        }

        service.updateProduct(id, request);

        return "redirect:/products/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.deleteLogical(id);

        return "redirect:/products";
    }
}
