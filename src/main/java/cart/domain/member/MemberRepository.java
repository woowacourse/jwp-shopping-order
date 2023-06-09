package cart.domain.member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findMemberById(Long id);

    Optional<Member> findMemberByEmail(String email);

    List<Member> findAllMembers();

    void addMember(Member member);

    void updateMember(Member member);

    void deleteMember(Long id);
}
