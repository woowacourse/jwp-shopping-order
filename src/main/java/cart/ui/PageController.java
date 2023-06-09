package cart.ui;

import cart.application.service.member.MemberReadService;
import cart.application.service.product.ProductReadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    private final ProductReadService productReadService;
    private final MemberReadService memberReadService;

    public PageController(ProductReadService productReadService, MemberReadService memberReadService) {
        this.productReadService = productReadService;
        this.memberReadService = memberReadService;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("products", productReadService.getAllProducts());
        return "admin";
    }

    @GetMapping("/settings")
    public String members(Model model) {
        model.addAttribute("members", memberReadService.findAllMembers());
        return "settings";
    }
}
