package cart.ui;

import cart.application.ProductService;
import cart.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

    private final ProductService productService;
    private final MemberRepository memberRepository;

    public PageController(ProductService productService, MemberRepository memberRepository) {
        this.productService = productService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/admin")
    public ModelAndView admin() {
        ModelAndView modelAndView = new ModelAndView("admin");
        modelAndView.addObject("products", productService.getAllProducts());
        return modelAndView;
    }

    @GetMapping("/settings")
    public ModelAndView members() {
        ModelAndView modelAndView = new ModelAndView("settings");
        modelAndView.addObject("members", memberRepository.findAllMembers());
        return modelAndView;
    }
}
