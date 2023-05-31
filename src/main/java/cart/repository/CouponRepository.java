package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.OrdersCouponDao;
import cart.dao.entity.CouponEntity;
import cart.domain.Coupon;
import cart.domain.CouponType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CouponRepository {
    private final CouponDao couponDao;
    private final OrdersCouponDao ordersCouponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponRepository(CouponDao couponDao, OrdersCouponDao ordersCouponDao, MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.ordersCouponDao = ordersCouponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public void issueCoupon(final long memberId, final long couponId) {
        if (memberCouponDao.findByMemberIdCouponId(memberId, couponId)) {
            return;
        }
        memberCouponDao.create(memberId, couponId);
    }
    public Coupon findById(final long id){
        return couponDao.findWithId(id);
    }
    public Map<Coupon, Boolean> findAllCoupons(final long id) {
        List<Long> memberCouponIds = memberCouponDao.findByMemberId(id);
        List<CouponEntity> couponEntities = couponDao.findAll();
        Map<Coupon, Boolean> coupons = new LinkedHashMap<>();
        for (CouponEntity coupon : couponEntities) {
            coupons.put(rendering(coupon), doesntHaveCoupon(memberCouponIds, coupon.getId()));
        }
        return coupons;
    }

    private boolean doesntHaveCoupon(List<Long> coupons, long couponId) {
        return !coupons.contains(couponId);
    }

    private Coupon rendering(CouponEntity couponEntity) {
        return new Coupon(
                couponEntity.getId(),
                couponEntity.getName(),
                CouponType.mappingType(couponEntity.getCouponType()),
                couponEntity.getDiscountRate(),
                couponEntity.getDiscountAmount(),
                couponEntity.getMinimumPrice()
        );
    }

    public List<Coupon> findCouponsByMemberId(Long id) {
        List<Long> memberCouponIds = memberCouponDao.findByMemberId(id);
        List<Coupon> coupons = new ArrayList<>();
        for (Long couponId : memberCouponIds) {
            coupons.add(couponDao.findWithId(couponId));
        }
        return coupons;
    }

    public List<Coupon> findCouponsById(final List<Long> ids){
        List<Coupon> coupons =new ArrayList<>();
        for(Long couponId : ids){
            coupons.add(couponDao.findWithId(couponId));
        }
        return coupons;
    }

    public List<Coupon> findByOrdersId(final long id){
        return   ordersCouponDao.finAllByOrdersId(id).stream()
                .map(ordersCouponEntity -> couponDao.findWithId(ordersCouponEntity.getCouponId()))
                .collect(Collectors.toList());
    }

    public void addOrdersCoupon(final long orderId, final List<Long> couponIds) {
        for (Long couponId : couponIds) {
            ordersCouponDao.createOrderCoupon(orderId, couponId);
        }
    }
}
