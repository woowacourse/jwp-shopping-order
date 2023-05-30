package cart.ui;

import cart.application.ProductService;
import cart.persistence.dao.MemberDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    private final ProductService productService;
    private final MemberDao memberDao;

    public PageController(ProductService productService, MemberDao memberDao) {
        this.productService = productService;
        this.memberDao = memberDao;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin";
    }

    @GetMapping("/settings")
    public String members(Model model) {
        model.addAttribute("members", memberDao.findAll());
        return "settings";
    }
}
