package cart.discountpolicy.application.builder;

import cart.cart.Cart;
import cart.discountpolicy.DiscountPolicy;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.product.Product;

public class DiscountFromTotalPricePolicy extends DiscountTargetPolicy {
    public DiscountFromTotalPricePolicy(DiscountCondition discountCondition, DiscountUnitPolicy discountUnitPolicy) {
        super(discountCondition, discountUnitPolicy);
    }

    @Override
    public void discount(Cart cart) {
        final var totalPrice = cart.calculateTotalPrice();
        final var discountPrice = discountUnitPolicy.calculateDiscountPrice(discountCondition.getDiscountValue(), totalPrice);
        cart.addDiscountFromTotalPrice(discountPrice);
    }

    @Override
    public void discount(Product product) {
        return;
    }
}
