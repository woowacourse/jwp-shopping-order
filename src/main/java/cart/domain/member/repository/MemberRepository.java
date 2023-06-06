package cart.domain.member.repository;

import cart.domain.member.Member;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(long id);

    Member save(Member member);
}
