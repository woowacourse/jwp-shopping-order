package cart.application;

import static java.util.stream.Collectors.toList;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.Coupon;
import cart.domain.MemberCoupon;
import cart.dto.CouponRequest;
import cart.dto.CouponResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponService(CouponDao couponDao, MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public Long create(CouponRequest request) {
        Coupon coupon = request.toEntity();
        return couponDao.save(coupon);
    }

    public Long createToMember(Long memberId, Long couponId) {
        Coupon coupon = couponDao.findById(couponId)
                .orElseThrow(IllegalArgumentException::new);
        MemberCoupon memberCoupon = new MemberCoupon(memberId, coupon);
        return memberCouponDao.save(memberCoupon);
    }

    public List<CouponResponse> findAll() {
        return couponDao.findAll().stream()
                .map(CouponResponse::from)
                .collect(toList());
    }

    public List<CouponResponse> findAllByMemberId(Long memberId) {
        return couponDao.findAllByMemberId(memberId).stream()
                .map(CouponResponse::from)
                .collect(toList());
    }
}
