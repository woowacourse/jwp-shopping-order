package com.woowahan.techcourse.product.ui;

import com.woowahan.techcourse.product.application.ProductQueryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    private final ProductQueryService productQueryService;

    public ProductController(ProductQueryService productQueryService) {
        this.productQueryService = productQueryService;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("products", productQueryService.getAllProducts());
        return "admin";
    }
}
