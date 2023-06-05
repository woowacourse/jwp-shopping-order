package cart.domain.memberCoupon;

import java.util.List;

public interface MemberCouponRepository {
    List<MemberCoupon> findAll();

    MemberCoupon findById(Long id);

    Long add(MemberCoupon memberCoupon);

    void delete(Long id);

    List<MemberCoupon> findMemberCouponsByMemberId(Long memberId);
}
