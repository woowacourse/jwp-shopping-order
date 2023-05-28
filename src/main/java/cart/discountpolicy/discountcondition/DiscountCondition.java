package cart.discountpolicy.discountcondition;

import cart.product.Product;

public class DiscountCondition {
    private final Product product;

    public DiscountCondition(Product product) {
        this.product = product;
    }

    public Category getCategory() {
        return product.getCategory();
    }
}
