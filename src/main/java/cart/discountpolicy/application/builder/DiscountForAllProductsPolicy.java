package cart.discountpolicy.application.builder;

import cart.cart.Cart;
import cart.cartitem.CartItem;
import cart.discountpolicy.discountcondition.DiscountCondition;

public class DiscountForAllProductsPolicy extends DiscountTargetPolicy {
    public DiscountForAllProductsPolicy(DiscountCondition discountCondition, DiscountUnitPolicy discountUnitPolicy) {
        super(discountCondition, discountUnitPolicy);
    }

    @Override
    public void discount(Cart cart) {
        for (CartItem cartItem : cart.getCartItems()) {
            cartItem.addDiscountPrice(discountUnitPolicy.calculateDiscountPrice(discountCondition.getDiscountValue(), cartItem.getOriginalPrice()));
        }
    }
}
