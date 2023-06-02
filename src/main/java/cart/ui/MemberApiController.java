package cart.ui;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.Point;
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
        if (memberRequest.getPoint() == null) {
            Member memberWithoutPoint = new Member(memberRequest.getEmail(), memberRequest.getPassword());
            memberDao.addMemberWithoutPoint(memberWithoutPoint);
            return ResponseEntity.ok().build();
        }
        Member memberWithPoint = new Member(memberRequest.getEmail(), memberRequest.getPassword(), new Point(memberRequest.getPoint()));
        memberDao.addMemberWithPoint(memberWithPoint);
        return ResponseEntity.ok().build();
    }
}
