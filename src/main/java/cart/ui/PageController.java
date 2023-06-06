package cart.ui;

import cart.application.PolicyService;
import cart.application.ProductService;
import cart.dao.MemberDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    private final ProductService productService;
    private final MemberDao memberDao;
    private final PolicyService policyService;

    public PageController(final ProductService productService, final MemberDao memberDao, final PolicyService policyService) {
        this.productService = productService;
        this.memberDao = memberDao;
        this.policyService = policyService;
    }

    @GetMapping("/admin")
    public String admin(final Model model) {
        model.addAttribute("products", this.productService.getAllProducts());
        return "admin";
    }

    @GetMapping("/settings")
    public String members(final Model model) {
        model.addAttribute("members", this.memberDao.getAllMembers());
        return "settings";
    }

    @GetMapping("/policy")
    public String policy(final Model model) {
        model.addAttribute("discountPolicies", this.policyService.getDefaultDiscountPolicy());
        model.addAttribute("deliveryPolicies", this.policyService.getDefaultDeliveryPolicy());
        return "policy";
    }
}
