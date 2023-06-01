package cart.domain;

import java.util.List;

public class OrderItems {

    private final List<OrderItem> orderItems;
    private final DiscountPriceCalculator discountPriceCalculator;

    public OrderItems(final List<OrderItem> orderItems, final DiscountPriceCalculator discountPriceCalculator) {
        this.orderItems = orderItems;
        this.discountPriceCalculator = discountPriceCalculator;
    }

    public Price calculateOriginalPrice() {
        return orderItems.stream()
                .map(OrderItem::calculateTotalPrice)
                .reduce(Price::add)
                .orElse(new Price(0));
    }

    public Price calculateDiscountPrice() {
        final Price originalPrice = calculateOriginalPrice();
        return discountPriceCalculator.calculate(originalPrice);
    }

    public boolean isPaymentAmountEqual(final Price price) {
        final Price paymentAmount = calculateOriginalPrice().subtract(calculateDiscountPrice());
        return price.equals(paymentAmount);
    }

    public List<OrderItem> getOrderItems() {
        return List.copyOf(orderItems);
    }
}
