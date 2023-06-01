package cart.ui;

import cart.dao.MemberDao;
import cart.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberApiController {
    private final MemberDao memberDao;

    public MemberApiController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @PostMapping
    public ResponseEntity<Void> addMember(@RequestBody MemberRequest memberRequest) {
        Member member = new Member(memberRequest.getEmail(), memberRequest.getPassword());
        memberDao.addMember(member);
        return ResponseEntity.ok().build();
    }
}
