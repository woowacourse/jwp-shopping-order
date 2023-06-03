package cart.application;

import static cart.application.mapper.MemberMapper.convertMemberCoupon;
import static cart.domain.coupon.CouponType.FIRST_ORDER_COUPON;
import static cart.domain.coupon.CouponType.JOIN_MEMBER_COUPON;

import cart.domain.coupon.CouponRepository;
import cart.domain.coupon.dto.CouponWithId;
import cart.domain.event.FirstOrderCouponEvent;
import cart.domain.event.JoinMemberCouponEvent;
import cart.domain.member.MemberCoupon;
import cart.domain.member.MemberCouponRepository;
import cart.domain.order.OrderRepository;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class MemberCouponService {

    private static final int FIRST_ORDER_COUNT = 1;

    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public MemberCouponService(final OrderRepository orderRepository, final CouponRepository couponRepository,
                               final MemberCouponRepository memberCouponRepository) {
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    @TransactionalEventListener
    public void saveJoinMemberCoupon(final JoinMemberCouponEvent joinMemberCouponEvent) {
        final Long memberId = joinMemberCouponEvent.getMemberId();
        final CouponWithId coupon = couponRepository.findByNameAndDiscountRate(JOIN_MEMBER_COUPON.getName(),
            JOIN_MEMBER_COUPON.getDiscountRate());

        final LocalDateTime issuedAt = LocalDateTime.now();
        validateAlreadyIssued(memberId, coupon.getCouponId());
        final MemberCoupon memberCoupon = convertMemberCoupon(coupon, issuedAt);
        memberCouponRepository.save(memberId, memberCoupon);
    }

    @TransactionalEventListener
    public void saveFirstOrderCoupon(final FirstOrderCouponEvent firstOrderCouponEvent) {
        final Long memberId = firstOrderCouponEvent.getMemberId();
        final CouponWithId coupon = couponRepository.findByNameAndDiscountRate(FIRST_ORDER_COUPON.getName(),
            FIRST_ORDER_COUPON.getDiscountRate());

        validateFirstOrder(memberId);
        final LocalDateTime issuedAt = LocalDateTime.now();
        validateAlreadyIssued(memberId, coupon.getCouponId());
        final MemberCoupon memberCoupon = convertMemberCoupon(coupon, issuedAt);
        memberCouponRepository.save(memberId, memberCoupon);
    }

    private void validateFirstOrder(final Long memberId) {
        final Long orderCount = orderRepository.countByMemberId(memberId);
        if (orderCount != FIRST_ORDER_COUNT) {
            throw new BadRequestException(ErrorCode.COUPON_NOT_FIRST_ORDER);
        }
    }

    private void validateAlreadyIssued(final Long memberId, final Long couponId) {
        if (memberCouponRepository.existByMemberIdAndCouponId(memberId, couponId)) {
            throw new BadRequestException(ErrorCode.COUPON_ALREADY_EXIST);
        }
    }
}
