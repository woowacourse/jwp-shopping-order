package cart.application;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Money;
import cart.domain.PriceCalculator;
import cart.dto.request.MemberCouponAddRequest;
import cart.dto.response.CouponResponse;
import cart.dto.response.CouponsResponse;
import cart.dto.response.MemberCouponResponse;
import cart.dto.response.MemberCouponsResponse;
import cart.exception.BadRequestException;
import cart.exception.ExceptionType;

@Service
public class CouponService {
    private final CartItemDao cartItemDao;

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponService(CartItemDao cartItemDao, CouponDao couponDao, MemberCouponDao memberCouponDao) {
        this.cartItemDao = cartItemDao;
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public MemberCouponsResponse findMemberCoupons(Member member, List<Long> cartItemIds) {
        List<CartItem> cartItems = cartItemDao.findByIds(cartItemIds);
        Money price = PriceCalculator.calculate(cartItems);
        List<MemberCoupon> memberCoupons = memberCouponDao.findByMemberId(member.getId());

        return new MemberCouponsResponse(
            memberCoupons.stream()
                .map(memberCoupon -> MemberCouponResponse.of(memberCoupon, price))
                .collect(Collectors.toUnmodifiableList()));
    }

    public CouponsResponse findAllCoupons() {
        List<Coupon> coupons = couponDao.findAll();
        return new CouponsResponse(coupons.stream().map(CouponResponse::of).collect(Collectors.toUnmodifiableList()));
    }

    @Transactional
    public Long addMemberCoupon(Member member, Long couponId, MemberCouponAddRequest memberCouponAddRequest) {
        Coupon coupon = couponDao.findById(couponId);
        checkExistence(coupon);

        MemberCoupon memberCoupon = new MemberCoupon(
            null,
            member,
            coupon,
            Timestamp.valueOf(memberCouponAddRequest.getExpiredAt())
        );

        return memberCouponDao.save(memberCoupon);
    }

    private void checkExistence(Coupon coupon) {
        if (coupon == null) {
            throw new BadRequestException(ExceptionType.COUPON_NO_EXIST);
        }
    }

}
