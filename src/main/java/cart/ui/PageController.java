package cart.ui;

import cart.application.ProductService;
import cart.dao.MemberDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    private final ProductService productService;
    private final MemberDao memberDao;

    public PageController(final ProductService productService, final MemberDao memberDao) {
        this.productService = productService;
        this.memberDao = memberDao;
    }

    @GetMapping("/admin")
    public String admin(final Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin";
    }

    @GetMapping("/settings")
    public String members(final Model model) {
        model.addAttribute("members", memberDao.getAllMembers());
        return "settings";
    }
}
