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

        final LocalDateTime issuedAt = LocalDateTime.now();
        validateExpiredCoupon(coupon, issuedAt);

        validateAlreadyIssued(memberId, coupon.getCouponId());

        final MemberCoupon memberCoupon = new MemberCoupon(coupon, issuedAt,
            issuedAt.plusDays(coupon.getCoupon().period()), false);
        memberCouponRepository.save(memberId, memberCoupon);
    }

    private void validateExpiredCoupon(final CouponWithId coupon, final LocalDateTime issuedAt) {
        if (coupon.getCoupon().expiredAt().isBefore(issuedAt)) {
            throw new BadRequestException(ErrorCode.COUPON_EXPIRED);
        }
    }

    private void validateAlreadyIssued(final Long memberId, final Long couponId) {
        if (memberCouponRepository.existByMemberIdAndCouponId(memberId, couponId)) {
            throw new BadRequestException(ErrorCode.COUPON_ALREADY_EXIST);
        }
    }
}
