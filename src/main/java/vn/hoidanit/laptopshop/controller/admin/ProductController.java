package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@Controller
public class ProductController {
    private final ProductService productService;
    private final UploadService uploadService;

    ProductController(ProductService productService, UploadService uploadService) {
        this.uploadService = uploadService;
        this.productService = productService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        List<Product> products = this.productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/product/show";
    }

    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product product = this.productService.geProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("id", id);

        return "admin/product/detail";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String createProductPage(
            @ModelAttribute("newProduct") @Valid Product hoidanit,
            BindingResult newProductBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {
        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }

        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }
        String avatar = this.uploadService.handleSaveUploadFile(file, "product");
        hoidanit.setImage(avatar);
        this.productService.handleSaveProduct(hoidanit);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(@ModelAttribute("newProduct") Product hoidanit) {
        this.productService.deleteAProduct(hoidanit.getId());
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Product newProduct = this.productService.geProductById(id);
        model.addAttribute("newProduct", newProduct);
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String postUpdateProduct(@ModelAttribute("newProduct") @Valid Product hoidanit,
            BindingResult newProductBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {
        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/update";
        }
        String avatar = this.uploadService.handleSaveUploadFile(file, "product");
        Product currenProduct = this.productService.geProductById(hoidanit.getId());
        if (avatar != "") {
            currenProduct.setImage(avatar);
        }
        currenProduct.setName(hoidanit.getName());
        currenProduct.setPrice(hoidanit.getPrice());
        currenProduct.setDetailDesc(hoidanit.getDetailDesc());
        currenProduct.setFactory(hoidanit.getFactory());
        currenProduct.setQuantity(hoidanit.getQuantity());
        currenProduct.setShortDesc(hoidanit.getShortDesc());
        currenProduct.setTarget(hoidanit.getTarget());
        this.productService.handleSaveProduct(currenProduct);
        return "redirect:/admin/product";
    }

    @PostMapping("/admin/product/{id}")
    public String getProductRegister(Model model) {
        return "";
    }

}
