package cart.coupon.domain;

import static cart.coupon.exception.CouponExceptionType.APPLY_MULTIPLE_TO_PRODUCT;
import static cart.coupon.exception.CouponExceptionType.EXIST_UNUSED_COUPON;
import static cart.coupon.exception.CouponExceptionType.NO_AUTHORITY_USE_COUPON;

import cart.cartitem.domain.CartItem;
import cart.coupon.exception.CouponException;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class CouponValidator {

    public void validate(Long memberId, List<CartItem> cartItems, List<Coupon> coupons) {
        validateOwner(memberId, coupons);
        validateMultipleApply(cartItems, coupons);
        validateUnusedCoupon(cartItems, coupons);
    }

    private void validateOwner(Long memberId, List<Coupon> coupons) {
        for (Coupon coupon : coupons) {
            if (!Objects.equals(memberId, coupon.getMemberId())) {
                throw new CouponException(NO_AUTHORITY_USE_COUPON);
            }
        }
    }

    private void validateMultipleApply(List<CartItem> cartItems, List<Coupon> coupons) {
        for (CartItem cartItem : cartItems) {
            long applyCount = coupons.stream()
                    .filter(coupon -> coupon.canApply(cartItem.getProductId()))
                    .count();
            if (applyCount > 1) {
                throw new CouponException(APPLY_MULTIPLE_TO_PRODUCT);
            }
        }
    }

    private void validateUnusedCoupon(List<CartItem> cartItems, List<Coupon> coupons) {
        for (Coupon coupon : coupons) {
            boolean unused = cartItems.stream()
                    .noneMatch(cartItem -> coupon.canApply(cartItem.getProductId()));
            if (unused) {
                throw new CouponException(EXIST_UNUSED_COUPON);
            }
        }
    }
}
