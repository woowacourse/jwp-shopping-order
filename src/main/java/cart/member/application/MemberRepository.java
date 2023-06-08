package cart.member.application;

import cart.member.domain.Member;
import java.util.List;

public interface MemberRepository {
    Member getMemberById(Long id);
    Member getMemberByEmail(String email);
    void addMemberWithoutPoint(Member member);
    void addMemberWithPoint(Member member);
    void updateMember(Member member);
    void updatePoint(Member member);
    void deleteMember(Long id);
    List<Member> getAllMembers();
}
