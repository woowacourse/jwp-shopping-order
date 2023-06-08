package cart.member.application;

import cart.member.Member;

import java.util.List;

public interface MemberRepository {
    Member getMemberById(Long id);

    Member getMemberByEmail(String email);

    Long addMember(Member member);

    void updateMember(Member member);

    void deleteMember(Long id);

    List<Member> getAllMembers();
}
