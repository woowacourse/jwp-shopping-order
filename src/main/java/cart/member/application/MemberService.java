package cart.member.application;

import cart.member.application.dto.MemberAddDto;
import cart.member.domain.Member;
import cart.member.domain.Point;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void addMember(MemberAddDto memberAddDto) {
        if (memberAddDto.getPoint() == null) {
            Member memberWithoutPoint = new Member(memberAddDto.getEmail(), memberAddDto.getPassword());
            memberRepository.addMemberWithoutPoint(memberWithoutPoint);
        }
        Member memberWithPoint = new Member(memberAddDto.getEmail(), memberAddDto.getPassword(),
                new Point(memberAddDto.getPoint()));
        memberRepository.addMemberWithPoint(memberWithPoint);
    }

    public List<Member> getAllMembers() {
        return memberRepository.getAllMembers();
    }
}
