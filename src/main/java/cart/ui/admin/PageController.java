package cart.ui.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cart.application.member.MemberQueryService;
import cart.application.product.ProductQueryService;

@Controller
public class PageController {
	private final ProductQueryService productQueryService;
	private final MemberQueryService memberQueryService;

	public PageController(ProductQueryService productQueryService, MemberQueryService memberQueryService) {
		this.productQueryService = productQueryService;
		this.memberQueryService = memberQueryService;
	}

	@GetMapping("/admin")
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
