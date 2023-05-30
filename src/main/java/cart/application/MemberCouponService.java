package cart.application;

import cart.domain.coupon.CouponRepository;
import cart.domain.coupon.CouponSaveEvent;
import cart.domain.coupon.dto.CouponWithId;
import cart.domain.member.MemberCoupon;
import cart.domain.member.MemberCouponRepository;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class MemberCouponService {

    private static final String JOIN_MEMBER_COUPON = "신규 가입 축하 쿠폰";
    private static final int JOIN_MEMBER_COUPON_DISCOUNT_RATE = 10;

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public MemberCouponService(final CouponRepository couponRepository,
                               final MemberCouponRepository memberCouponRepository) {
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    @TransactionalEventListener
    public void saveMemberCoupon(final CouponSaveEvent couponSaveEvent) {
        final Long memberId = couponSaveEvent.getMemberId();
        final CouponWithId coupon = couponRepository.findByNameAndDiscountRate(JOIN_MEMBER_COUPON,
            JOIN_MEMBER_COUPON_DISCOUNT_RATE);
        final LocalDateTime issuedDate = LocalDateTime.now();
        if (coupon.getExpiredDate().isBefore(issuedDate)) {
            throw new BadRequestException(ErrorCode.COUPON_EXPIRED);
        }
        final MemberCoupon memberCoupon = new MemberCoupon(coupon, issuedDate, issuedDate.plusDays(coupon.getPeriod()),
            false);
        memberCouponRepository.save(memberId, memberCoupon);
    }
}
