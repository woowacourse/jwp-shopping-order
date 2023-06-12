package cart.domain.repository;

import cart.domain.member.Member;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> getMemberById(long memberId);

    void updateMoney(long memberId, int money);

    void updatePoint(long memberId, int updatePoint);

    Optional<Member> getMemberByEmail(String email);
}
