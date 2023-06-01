package cart.service;

import cart.dao.CouponDao;
import cart.domain.coupon.Coupon;
import cart.domain.Member;
import cart.dto.CouponResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final CouponDao couponDao;

    public CouponService(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public List<CouponResponse> findByMember(Member member) {
        List<Coupon> coupons = couponDao.findByMemberId(member.getId());
        return coupons.stream()
                .map(CouponResponse::of)
                .collect(Collectors.toList());
    }

    public List<Coupon> findByIds(List<Long> ids) {
        return ids.stream()
                .map(id -> couponDao.getCouponById(id))
                .collect(Collectors.toList());
    }
}
