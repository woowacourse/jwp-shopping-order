package cart.domain;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findMemberById(final Long id);

    Optional<Member> findMemberByEmail(final String email);

    List<Member> findAllMembers();

    Long saveMember(Member member);
}
