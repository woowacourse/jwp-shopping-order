package cart.domain.repository;

import cart.domain.member.Member;

public interface MemberRepository {
    Long save(Member member);

    Member findById(Long id);

    Member findByName(String name);

    boolean hasMember(Member member);
}
