package cart.domain;

import cart.domain.value.Money;
import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final List<OrderItem> orderItems;
    private final Money shippingFee;
    private final LocalDateTime orderedAt;
    private final Member member;

    public Order(
            final List<OrderItem> orderItems,
            final Member member
    ) {
        this.id = null;
        this.orderItems = orderItems;
        this.shippingFee = new Money(calculateShippingFee());
        this.orderedAt = LocalDateTime.now();
        this.member = member;
    }

    public Order(
            final Long id,
            final List<OrderItem> orderItems,
            final int shippingFee,
            final LocalDateTime orderedAt,
            final Member member
    ) {
        this.id = id;
        this.orderItems = orderItems;
        this.shippingFee = new Money(shippingFee);
        this.orderedAt = orderedAt;
        this.member = member;
    }

    public int calculateTotalPrice() {
        return calculateDiscountedTotalItemPrice() + calculateShippingFee();
    }

    public int calculateTotalItemDiscountAmount(final List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToInt(OrderItem::calculateDiscountAmount)
                .sum();
    }

    public int calculateTotalMemberDiscountAmount(final List<OrderItem> orderItems) {
        if (calculateTotalItemDiscountAmount(orderItems) != 0) {
            return 0;
        }
        return orderItems.stream()
                .mapToInt(orderItem ->
                        orderItem.calculateMemberDiscountAmount(member.getGrade().getDiscountRate()))
                .sum();
    }

    public int calculateDiscountedTotalItemPrice() {
        int sum = 0;
        for (final OrderItem orderItem : orderItems) {
            sum = getMemberDiscount(sum, orderItem);
            sum = getProductDiscount(sum, orderItem);
        }
        return sum;
    }

    private int getMemberDiscount(int sum, final OrderItem orderItem) {
        if (orderItem.isMemberDiscount()) {
            final double discountRate = member.getGrade().getDiscountRate();
            sum += orderItem.calculateDiscountedPriceBy(discountRate);
        }
        return sum;
    }

    private int getProductDiscount(int sum, final OrderItem orderItem) {
        if (orderItem.isProductDiscount()) {
            sum += orderItem.calculateDiscountedPrice();
        }
        return sum;
    }

    public int calculateTotalItemPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getPrice)
                .sum();
    }

    public int calculateShippingFee() {
        if (calculateDiscountedTotalItemPrice() < 50_000) {
            return 3_000;
        }
        return 0;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public int getShippingFee() {
        return shippingFee.getValue();
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public Member getMember() {
        return member;
    }
}
