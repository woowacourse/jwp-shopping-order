package cart.ui;

import cart.application.MemberResponse;
import cart.application.MemberService;
import cart.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberResponse> getRank(Member member){
        return ResponseEntity.ok(memberService.getRankById(member.getId()));
    }
}
