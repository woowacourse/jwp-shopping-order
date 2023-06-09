package cart.domain.respository.member;

import cart.domain.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> getMemberById(Long id);

    Optional<Member> getMemberByEmail(String email);

    void addMember(Member member);

    void updateMember(Member member);

    void deleteMember(Long id);

    List<Member> getAllMembers();
}
