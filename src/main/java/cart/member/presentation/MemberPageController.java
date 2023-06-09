package cart.member.presentation;

import cart.member.application.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberPageController {

    private final MemberService memberService;

    public MemberPageController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/settings")
    public String members(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "settings";
    }
}
