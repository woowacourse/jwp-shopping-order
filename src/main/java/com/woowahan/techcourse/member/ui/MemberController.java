package com.woowahan.techcourse.member.ui;

import com.woowahan.techcourse.member.application.MemberQueryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    private final MemberQueryService memberQueryService;

    public MemberController(MemberQueryService memberQueryService) {
        this.memberQueryService = memberQueryService;
    }

    @GetMapping("/settings")
    public String members(Model model) {
        model.addAttribute("members", memberQueryService.getAllMembers());
        return "settings";
    }
}
