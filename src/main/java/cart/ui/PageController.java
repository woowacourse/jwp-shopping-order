package cart.ui;

import cart.dao.MemberDao;
import cart.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {
    private final ProductService productService;
    private final MemberDao memberDao;

    public PageController(ProductService productService, MemberDao memberDao) {
        this.productService = productService;
        this.memberDao = memberDao;
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
        modelAndView.addObject("members", memberDao.getAllMembers());
        return modelAndView;
    }
}
