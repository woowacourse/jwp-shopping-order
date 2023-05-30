package cart.repository;

import cart.domain.Member;

public interface MemberRepository {

    Member findMemberByMemberIdWithCoupons(final Long memberId);
}
