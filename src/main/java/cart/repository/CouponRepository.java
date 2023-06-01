package cart.repository;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.Coupon;
import cart.domain.MemberCoupon;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponRepository(CouponDao couponDao, MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public List<MemberCoupon> findAllByMemberId(Long memberId) {
        List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findAllByMemberId(memberId);
        Set<Long> couponIds = memberCouponEntities.stream()
                .map(MemberCouponEntity::getCouponId)
                .collect(toSet());
        Map<Long, Coupon> coupons = couponDao.findAllByIds(couponIds).stream()
                .map(CouponEntity::toDomain)
                .collect(toMap(Coupon::getId, Function.identity()));
        return memberCouponEntities.stream()
                .map(it -> new MemberCoupon(it.getId(), it.getMemberId(), coupons.get(it.getCouponId()), it
                        .isUsed()))
                .collect(toList());
    }
}
