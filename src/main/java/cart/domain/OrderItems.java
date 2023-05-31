package cart.domain;

import java.util.List;

public class OrderItems {

    private final List<OrderItem> orderItems;
    private final DiscountPriceCalculator discountPriceCalculator;

    public OrderItems(final List<OrderItem> orderItems, final DiscountPriceCalculator discountPriceCalculator) {
        this.orderItems = orderItems;
        this.discountPriceCalculator = discountPriceCalculator;
    }

    public Price calculateTotalPrice() {
        return null;
    }

    public Price calculateDiscountPrice() {
        return null;
    }
}
