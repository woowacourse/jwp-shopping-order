package cart.discountpolicy.application.builder;

import cart.cart.Cart;
import cart.discountpolicy.discountcondition.DiscountCondition;

public class DiscountFromTotalPricePolicy extends DiscountTargetPolicy {
    public DiscountFromTotalPricePolicy(DiscountCondition discountCondition, DiscountUnitPolicy discountUnitPolicy) {
        super(discountCondition, discountUnitPolicy);
    }

    @Override
    public void discount(Cart cart) {
        final var totalPrice = cart.calculateProductsTotalPrice();
        cart.setDiscountFromTotalPrice(discountUnitPolicy.calculateDiscountPrice(discountCondition.getDiscountValue(), totalPrice));
    }
}
