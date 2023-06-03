package cart.controller;

import cart.repository.MemberRepository;
import cart.service.ProductService;
import org.springframework.stereotype.Controller;

@Controller
public class PageController {
    private final ProductService productService;
    private final MemberRepository memberRepository;

    public PageController(ProductService productService, MemberRepository memberRepository) {
        this.productService = productService;
        this.memberRepository = memberRepository;
    }

//    @GetMapping("/admin")
//    public String admin(Model model) {
//        model.addAttribute("products", productService.getAllProducts());
//        return "admin";
//    }
//
//    @GetMapping("/settings")
//    public String members(Model model) {
//        model.addAttribute("members", memberRepository.getAllMembers());
//        return "settings";
//    }
}
