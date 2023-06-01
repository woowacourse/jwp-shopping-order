package cart.ui;

import cart.application.ProductService;
import cart.dao.MemberDao;
import cart.dto.product.ProductResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

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
        final List<ProductResponse> productResponses = productService.getAllProducts().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        model.addAttribute("products", productResponses);
        return "admin";
    }

    @GetMapping("/settings")
    public String members(Model model) {
        model.addAttribute("members", memberDao.findAll());
        return "settings";
    }
}
