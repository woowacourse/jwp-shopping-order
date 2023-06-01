package cart.discountpolicy.application.builder;

import cart.cart.Cart;
import cart.discountpolicy.discountcondition.DiscountCondition;

public class DiscountForDeliveryPolicy extends DiscountTargetPolicy {
    public DiscountForDeliveryPolicy(DiscountCondition discountCondition, DiscountUnitPolicy discountUnitPolicy) {
        super(discountCondition, discountUnitPolicy);
    }

    @Override
    public void discount(Cart cart) {
        final var deliveryPrice = cart.getDeliveryPrice();
        final var discountedDeliveryPrice = discountUnitPolicy.calculateDiscountPrice(discountCondition.getDiscountValue(), deliveryPrice);
        cart.setDeliveryPrice(deliveryPrice - discountedDeliveryPrice);
    }
}
