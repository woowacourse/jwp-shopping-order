package cart.discountpolicy.application.builder;

import cart.cart.Cart;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.product.Product;

public class DiscountForDeliveryPolicy extends DiscountTargetPolicy {
    public DiscountForDeliveryPolicy(DiscountCondition discountCondition, DiscountUnitPolicy discountUnitPolicy) {
        super(discountCondition, discountUnitPolicy);
    }

    @Override
    public void discount(Cart cart) {
        final var discountDeliveryPrice = discountUnitPolicy.calculateDiscountPrice(discountCondition.getDiscountValue(), cart.getOriginalDeliveryPrice());
        cart.addDiscountForDeliveryPrice(discountDeliveryPrice);
    }

    @Override
    public void discount(Product product) {
        return;
    }
}
