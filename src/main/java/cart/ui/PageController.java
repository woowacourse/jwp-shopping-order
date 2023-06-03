package cart.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cart.application.CouponService;
import cart.application.ProductService;
import cart.dao.MemberDao;
import cart.dto.CouponResponse;

@Controller
public class PageController {
    private final ProductService productService;
    private final CouponService couponService;
    private final MemberDao memberDao;

    public PageController(ProductService productService, CouponService couponService, MemberDao memberDao) {
        this.productService = productService;
        this.couponService = couponService;
        this.memberDao = memberDao;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("coupons", CouponResponse.of(couponService.getCoupons()));
        return "admin";
    }

    @GetMapping("/settings")
    public String members(Model model) {
        model.addAttribute("members", memberDao.getAllMembers());
        return "settings";
    }
}
