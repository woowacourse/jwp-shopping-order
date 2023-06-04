package cart.ui.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cart.application.coupon.CouponQueryService;
import cart.application.member.MemberQueryService;
import cart.application.product.ProductQueryService;

@Controller
@RequestMapping("/admin")
public class PageController {
	private final ProductQueryService productQueryService;
	private final MemberQueryService memberQueryService;
	private final CouponQueryService couponQueryService;

	public PageController(ProductQueryService productQueryService, MemberQueryService memberQueryService,
		final CouponQueryService couponQueryService) {
		this.productQueryService = productQueryService;
		this.memberQueryService = memberQueryService;
		this.couponQueryService = couponQueryService;
	}

	@GetMapping
	public String admin(Model model) {
		model.addAttribute("products", productQueryService.getAllProducts());
		return "admin";
	}

	@GetMapping("/settings")
	public String members(Model model) {
		model.addAttribute("members", memberQueryService.findAll());
		return "settings";
	}
}
