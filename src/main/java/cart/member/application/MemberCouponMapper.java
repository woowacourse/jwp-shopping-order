package cart.member.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberCouponMapper {
    private Map<Long, List<Long>> memberCouponMap = new HashMap<>();


    public void addCoupon(Long memberId, Long couponId) {
        memberCouponMap.computeIfAbsent(memberId, k -> new ArrayList<>());
        memberCouponMap.get(memberId).add(couponId);
    }

    public List<Long> findAllByMemberId(Long memberId) {
        if (memberCouponMap.get(memberId) == null) {
            return List.of();
        }
        return memberCouponMap.get(memberId);
    }
}
