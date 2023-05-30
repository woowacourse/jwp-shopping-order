package cart.domain;

public class Order {

    private final Member member;
    private final Integer usedPoint;
    private final OrderProducts orderProducts;

    public Order(final Member member, final Integer usedPoint, final OrderProducts orderProducts) {
        this.member = member;
        this.usedPoint = usedPoint;
        this.orderProducts = orderProducts;
    }

    public Member getMember() {
        return member;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public OrderProducts getOrderProducts() {
        return orderProducts;
    }

    public Integer getTotalAmount() {
        return orderProducts.getTotalAmount();
    }

    public Integer getSavedPoint() {
        return orderProducts.getSavedPoint();
    }
}
