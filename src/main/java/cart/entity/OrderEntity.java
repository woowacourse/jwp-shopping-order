package cart.entity;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final Integer payment;
    private final Integer discountPoint;

    public OrderEntity(final Long id, final Long memberId, final Integer payment, final Integer discountPoint) {
        this.id = id;
        this.memberId = memberId;
        this.payment = payment;
        this.discountPoint = discountPoint;
    }

    public OrderEntity(final Long memberId, final Integer payment, final Integer discountPoint) {
        this(null, memberId, payment, discountPoint);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Integer getPayment() {
        return payment;
    }

    public Integer getDiscountPoint() {
        return discountPoint;
    }
}
