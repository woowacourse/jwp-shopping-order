package cart.repository;

import cart.domain.Member;

public interface MemberRepository {

    Member findByEmail(final String email);
    Member findById(final long id);
}
