package cart.presentation;

import cart.service.ProductService;
import cart.dao.MemberDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class PageController {

    private final ProductService productService;
    private final MemberDao memberDao;

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