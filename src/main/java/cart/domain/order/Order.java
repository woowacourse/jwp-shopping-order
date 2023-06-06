package cart.domain.order;

import cart.domain.cartitem.CartItem;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface Order {

    Long getOrderId();

    Member getMember();

    Optional<Coupon> getCoupon();

    List<CartItem> getCartItems();

    Integer getDeliveryPrice();

    LocalDateTime getOrderedAt();

    BigDecimal getTotalPrice();

    BigDecimal getDiscountedTotalPrice();

    Boolean isValid();

    boolean isNotOwner(final String memberName);
}
