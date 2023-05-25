package cart.ui;

import cart.application.ProductService;
import cart.dao.MemberDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.sql.DataSource;
import java.sql.SQLException;

@Controller
public class PageController {
    private final ProductService productService;
    private final MemberDao memberDao;
    private final DataSource dataSource;

    public PageController(ProductService productService, MemberDao memberDao, final DataSource dataSource) {
        this.productService = productService;
        this.memberDao = memberDao;
        this.dataSource = dataSource;
    }

    @GetMapping("/admin")
    public String admin(Model model) throws SQLException {
        model.addAttribute("products", productService.getAllProducts());
        dataSource.getConnection().getMetaData().getURL();
        return "admin";
    }

    @GetMapping("/settings")
    public String members(Model model) {
        model.addAttribute("members", memberDao.getAllMembers());
        return "settings";
    }
}
