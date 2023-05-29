package cart.dao;

import cart.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberDao {

    Optional<Member> findMemberById(Long id);

    Optional<Member> findMemberByEmail(String email);

    List<Member> findAllMembers();

    void addMember(Member member);

    void updateMember(Member member);

    void deleteMember(Long id);
}
