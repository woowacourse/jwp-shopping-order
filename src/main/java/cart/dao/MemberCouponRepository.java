package cart.dao;

import cart.domain.MemberCoupon;

import java.util.List;

public interface MemberCouponRepository {
    /* admin
     * 모든 쿠폰 정보 조회
     * 특정 쿠폰 정보 조회
     * 쿠폰 추가
     * 쿠폰 삭제
     * 쿠폰 업데이트
     */

    void save(MemberCoupon memberCoupon);

    List<MemberCoupon> findAllByMemberId(Long memberId);
}
