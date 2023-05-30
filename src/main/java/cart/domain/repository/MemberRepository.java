package cart.domain.repository;

import cart.domain.member.Member;

import java.util.List;

public interface MemberRepository {
    Long save(Member member);

    List<Member> findAll();

    Member findById(Long id);

    Member findByName(String name);
}
