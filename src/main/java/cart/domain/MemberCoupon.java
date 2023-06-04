package cart.domain;

import java.time.LocalDateTime;

public interface MemberCoupon {

    Integer calculateDiscount(final Integer totalPrice);

    MemberCoupon use();

    void checkOwner(final Member member);

    void checkExpired();

    Long getId();

    Member getMember();

    Coupon getCoupon();

    LocalDateTime getExpiredAt();

    LocalDateTime getCreatedAt();
}
