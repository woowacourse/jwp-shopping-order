package cart.application.mapper;

import cart.domain.cartitem.CartItemWithId;
import cart.domain.member.MemberCoupon;
import cart.domain.member.dto.MemberWithId;
import cart.domain.order.BasicOrder;
import cart.domain.order.CouponOrder;
import java.time.LocalDateTime;
import java.util.List;

public class OrderMapper {

    public static BasicOrder converBasicOrder(final List<CartItemWithId> productWithIds,
                                              final int deliveryPrice, final MemberWithId memberWithId) {
        return new BasicOrder(memberWithId, deliveryPrice, LocalDateTime.now(), productWithIds);
    }

    public static CouponOrder convertCouponOrder(final List<CartItemWithId> productWithIds,
                                                 final int deliveryPrice, final MemberWithId memberWithId,
                                                 final MemberCoupon memberCoupon) {
        return new CouponOrder(memberWithId, memberCoupon.getCoupon(), deliveryPrice,
            LocalDateTime.now(), productWithIds);
    }
}
