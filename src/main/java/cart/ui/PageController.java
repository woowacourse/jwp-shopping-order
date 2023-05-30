package cart.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cart.application.product.ProductQueryService;
import cart.domain.member.MemberRepository;

@Controller
public class PageController {
	private final ProductQueryService productQueryService;
	private final MemberRepository memberRepository;

	public PageController(ProductQueryService productQueryService, MemberRepository memberRepository) {
		this.productQueryService = productQueryService;
		this.memberRepository = memberRepository;
	}

	@GetMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("products", productQueryService.getAllProducts());
		return "admin";
	}

	@GetMapping("/settings")
	public String members(Model model) {
		model.addAttribute("members", memberRepository.findAll());
		return "settings";
	}
}
