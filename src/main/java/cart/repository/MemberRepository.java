package cart.repository;

import cart.domain.Member;

import java.util.List;

public interface MemberRepository {

    Member getMemberById(Long id);

    Member getMemberByEmail(String email);

    void addMember(Member member);

    void updateMember(Member member);

    void deleteMember(Long id);

    List<Member> getAllMembers();
}
