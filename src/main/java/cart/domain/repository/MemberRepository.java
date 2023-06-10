package cart.domain.repository;

import cart.domain.Member;
import java.util.List;

public interface MemberRepository {
    Member findById(final Long id);

    Member findByEmail(final String email);

    List<Member> findAll();
}
