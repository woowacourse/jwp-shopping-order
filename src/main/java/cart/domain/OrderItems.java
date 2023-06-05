package cart.domain;

import cart.domain.discount_strategy.DiscountCalculator;
import cart.domain.discount_strategy.DiscountPriceCalculator;

import java.util.List;

public class OrderItems {

    private final List<OrderItem> orderItems;
    private final DiscountCalculator discountCalculator;

    public OrderItems(final List<OrderItem> orderItems, final DiscountCalculator discountCalculator) {
        this.orderItems = orderItems;
        this.discountCalculator = discountCalculator;
    }

    public Price calculateOriginalPrice() {
        return orderItems.stream()
                .map(OrderItem::calculateTotalPrice)
                .reduce(Price::add)
                .orElse(new Price(0));
    }

    public Price calculateDiscountPrice() {
        final Price originalPrice = calculateOriginalPrice();
        return discountCalculator.calculate(originalPrice);
    }

    public Price calculatePaymentAmount() {
        return calculateOriginalPrice().subtract(calculateDiscountPrice());
    }

    public boolean isPaymentAmountEqual(final Price price) {
        return price.equals(calculatePaymentAmount());
    }

    public List<OrderItem> getOrderItems() {
        return List.copyOf(orderItems);
    }
}
