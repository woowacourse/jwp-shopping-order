package cart.ui;

import cart.application.ProductService;
import cart.dao.MemberDao;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "page", description = "page 컨트롤러")
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
        model.addAttribute("members", memberDao.getAllMembers());
        return "settings";
    }
}
