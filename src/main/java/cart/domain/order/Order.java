package cart.domain.order;

import cart.domain.cartitem.dto.CartItemWithId;
import cart.domain.coupon.dto.CouponWithId;
import cart.domain.member.dto.MemberWithId;
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
}
