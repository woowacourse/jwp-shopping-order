package cart.domain.order;

import cart.domain.cartitem.CartItemWithId;
import cart.domain.coupon.CouponWithId;
import cart.domain.member.MemberWithId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface Order {

    MemberWithId getMember();

    Optional<CouponWithId> getCoupon();

    List<CartItemWithId> getCartItems();

    Integer getDeliveryPrice();

    LocalDateTime getOrderedAt();

    BigDecimal getTotalPrice();

    BigDecimal getDiscountedTotalPrice();

    Boolean isValid();
    boolean isOwner(final String memberName);
}
