package cart.controller;

import cart.dto.MemberDto;
import cart.dto.ProductResponse;
import cart.service.MemberService;
import cart.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PageController {

    private final ProductService productService;
    private final MemberService memberService;

    public PageController(final ProductService productService, final MemberService memberService) {
        this.productService = productService;
        this.memberService = memberService;
    }

    @GetMapping("/admin")
    public String admin(final Model model) {
        final List<ProductResponse> products = productService.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        model.addAttribute("products", products);
        return "admin";
    }

    @GetMapping("/settings")
    public String settings(final Model model) {
        final List<MemberDto> members = memberService.findAll().stream()
                .map(MemberDto::from)
                .collect(Collectors.toList());
        model.addAttribute("members", members);
        return "settings";
    }
}
