package cart.domain;

public class Order {

    private final Long id;
    private final long memberId;
    private final OrderItems orderItems;

    public Order(final Long id, final long memberId, final OrderItems orderItems) {
        this.id = id;
        this.memberId = memberId;
        this.orderItems = orderItems;
    }

    public Order(final long memberId, final OrderItems orderItems) {
        this(null, memberId, orderItems);
    }

    public boolean isMemberIdEqual(final long memberId) {
        return this.memberId == memberId;
    }

    public boolean isPaymentAmountEqual(final int amount) {
        return orderItems.isPaymentAmountEqual(new Price(amount));
    }

    public int getOriginalPrice() {
        return orderItems.calculateOriginalPrice().getAmount();
    }

    public int getDiscountPrice() {
        return orderItems.calculateDiscountPrice().getAmount();
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }
}
