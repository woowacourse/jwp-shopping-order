package cart.member.application;

import cart.member.application.dto.MemberAddDto;
import cart.member.domain.Member;
import cart.member.domain.Point;
import cart.member.persistence.MemberDao;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void addMember(MemberAddDto memberAddDto) {
        if (memberAddDto.getPoint() == null) {
            Member memberWithoutPoint = new Member(memberAddDto.getEmail(), memberAddDto.getPassword());
            memberDao.addMemberWithoutPoint(memberWithoutPoint);
        }
        Member memberWithPoint = new Member(memberAddDto.getEmail(), memberAddDto.getPassword(),
                new Point(memberAddDto.getPoint()));
        memberDao.addMemberWithPoint(memberWithPoint);
    }

    public List<Member> getAllMembers() {
        return memberDao.getAllMembers();
    }
}
