package cart.entity;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final int totalPrice;
    private final int payPrice;
    private final int earnedPoints;
    private final int usedPoints;
    private final String orderDate;

    public OrderEntity(final Long id, final Long memberId, final int totalPrice, final int payPrice, final int earnedPoints, final int usedPoints, final String orderDate) {
        this.id = id;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.payPrice = payPrice;
        this.earnedPoints = earnedPoints;
        this.usedPoints = usedPoints;
        this.orderDate = orderDate;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPayPrice() {
        return payPrice;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public int getUsedPoints() {
        return usedPoints;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
