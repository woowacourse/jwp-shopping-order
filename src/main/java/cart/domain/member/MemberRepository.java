package cart.domain.member;

public interface MemberRepository {

    Member findMemberByMemberIdWithCoupons(final Long memberId);
}
