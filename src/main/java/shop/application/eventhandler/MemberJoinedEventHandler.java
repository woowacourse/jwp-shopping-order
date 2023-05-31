package shop.application.eventhandler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import shop.application.coupon.CouponService;
import shop.domain.coupon.CouponType;
import shop.domain.event.MemberJoinedEvent;

@Component
public class MemberJoinedEventHandler {
    private final CouponService couponService;

    public MemberJoinedEventHandler(CouponService couponService) {
        this.couponService = couponService;
    }

    @EventListener(MemberJoinedEvent.class)
    public void issueWelcomeCoupon(MemberJoinedEvent event) {
        couponService.issueCoupon(event.getMemberId(), CouponType.WELCOME_JOIN);
    }
}
