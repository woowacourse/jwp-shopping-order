package cart.ui;

import cart.repository.MemberRepository;
import cart.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    private final ProductService productService;
    private final MemberRepository memberRepository;

    public PageController(ProductService productService, MemberRepository memberRepository) {
        this.productService = productService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin";
    }

    @GetMapping("/settings")
    public String members(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "settings";
    }
}
