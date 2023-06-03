package cart.discountpolicy.application.builder;

import cart.cart.Cart;
import cart.cartitem.CartItem;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.product.Product;

public class DiscountForSpecificProductsPolicy extends DiscountTargetPolicy {
    public DiscountForSpecificProductsPolicy(DiscountCondition discountCondition, DiscountUnitPolicy discountUnitPolicy) {
        super(discountCondition, discountUnitPolicy);
    }

    @Override
    public void discount(Cart cart) {
        for (CartItem cartItem : cart.getCartItems()) {
            if (discountCondition.getDiscountTargetProductIds().contains(cartItem.getProductId())) {
                cartItem.addDiscountPrice(discountUnitPolicy.calculateDiscountPrice(discountCondition.getDiscountValue(), cartItem.getOriginalPrice()));
            }
        }
    }

    @Override
    public void discount(Product product) {
        if (discountCondition.getDiscountTargetProductIds().contains(product.getId())) {
            product.addDiscountPrice(discountUnitPolicy.calculateDiscountPrice(discountCondition.getDiscountValue(), product.getPrice()));
        }
    }
}
