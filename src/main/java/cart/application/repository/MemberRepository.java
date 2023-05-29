package cart.application.repository;

import cart.domain.Member;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);
}
