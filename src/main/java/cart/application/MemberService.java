package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.Point;
import cart.ui.MemberRequest;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void addMember(MemberRequest memberRequest) {
        if (memberRequest.getPoint() == null) {
            Member memberWithoutPoint = new Member(memberRequest.getEmail(), memberRequest.getPassword());
            memberDao.addMemberWithoutPoint(memberWithoutPoint);
        }
        Member memberWithPoint = new Member(memberRequest.getEmail(), memberRequest.getPassword(),
                new Point(memberRequest.getPoint()));
        memberDao.addMemberWithPoint(memberWithPoint);
    }

    public List<Member> getAllMembers() {
        return memberDao.getAllMembers();
    }
}
