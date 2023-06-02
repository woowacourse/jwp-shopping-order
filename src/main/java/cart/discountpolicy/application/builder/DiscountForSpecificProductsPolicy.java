package cart.discountpolicy.application.builder;

import cart.cart.Cart;
import cart.cartitem.CartItem;
import cart.discountpolicy.discountcondition.DiscountCondition;

public class DiscountForSpecificProductsPolicy extends DiscountTargetPolicy {
    public DiscountForSpecificProductsPolicy(DiscountCondition discountCondition, DiscountUnitPolicy discountUnitPolicy) {
        super(discountCondition, discountUnitPolicy);
    }

    @Override
    public void discount(Cart cart) {
        for (CartItem cartItem : cart.getCartItems()) {
            if (discountCondition.getDiscountTargetProductIds().contains(cartItem.getProduct().getId())) {
                cartItem.setDiscountPrice(discountUnitPolicy.calculateDiscountPrice(discountCondition.getDiscountValue(), cartItem.getProduct().getPrice()));
            }
        }
    }
}
