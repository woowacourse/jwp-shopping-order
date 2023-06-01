package cart.repository;

import cart.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    List<Member> findAll();

    Long create(Member member);
}
