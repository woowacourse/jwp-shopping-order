package cart.domain.order;

import cart.domain.cartitem.CartItemWithId;
import cart.domain.coupon.dto.CouponWithId;
import cart.domain.member.dto.MemberWithId;
import java.time.LocalDateTime;
import java.util.List;

public interface Order {

    MemberWithId getMember();

    CouponWithId getCoupon();

    List<CartItemWithId> getCartItems();

    Integer getDeliveryPrice();

    LocalDateTime getOrderDate();

    Integer getTotalPrice();

    Integer getDiscountedTotalPrice();
}
