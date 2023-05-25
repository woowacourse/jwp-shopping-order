package cart.member.presentation;

import cart.member.infrastructure.persistence.dao.MemberDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {

    private final MemberDao memberDao;

    public MemberViewController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @GetMapping("/settings")
    public String members(Model model) {
        model.addAttribute("members", memberDao.getAllMembers());
        return "settings";
    }
}
