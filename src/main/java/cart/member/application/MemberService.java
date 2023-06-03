package cart.member.application;

import cart.common.dto.MemberRequest;
import cart.member.persistence.MemberDao;
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
